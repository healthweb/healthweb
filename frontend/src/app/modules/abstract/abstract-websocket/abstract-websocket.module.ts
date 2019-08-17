import {NgModule, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
// @ts-ignore
import WebSocketAsPromised from "websocket-as-promised";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {filter, flatMap, map} from "rxjs/operators";

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export abstract class AbstractWebsocketModule<K, T> implements OnDestroy {

  private readonly wsp: WebSocketAsPromised;

  private readonly dataMap: Map<K, T> = new Map<K, T>();
  private readonly subjectMap: BehaviorSubject<Map<K, T>> = new BehaviorSubject<Map<K, T>>(this.dataMap);

  protected constructor(private path: string, private key: (T) => K, protected http: HttpClient) {
    let wsUrl = `${window.location.hostname}:${window.location.port}${path}`;
    if (location.protocol == 'https:') {
      this.wsp = new WebSocketAsPromised(`wss://${wsUrl}`);
    } else {
      this.wsp = new WebSocketAsPromised(`ws://${wsUrl}`);
    }
    this.wsp.onMessage.addListener(msg => {
      //console.debug(`GOT MESSAGE! ${path}`);
      let t: T = JSON.parse(msg);
      let k: K = this.key(t);
      this.publish(k, t);
    });
    this.wsp.onClose.addListener(() => {
      console.warn("WebSockets disconnected");
      setTimeout(() => {
        console.info("WebSockets reconnecting");
        (async () => await this.connectWsAsync())();
      }, 5_000)
    });
    (async () => await this.connectWsAsync())();
  }

  private publish(k: K, t: T) {
    this.dataMap.set(k, t);
    this.subjectMap.next(this.dataMap);
  }

  public getMap(): Observable<Map<K, T>> {
    return this.subjectMap
  }

  public getArr(): Observable<T[]> {
    return this.getMap().pipe(map((m: Map<K, T>) => {
      return Array.from(m.values())
    }));
  }

  public getSnapshotMap(): Map<K, T> {
    return this.subjectMap.getValue();
  }

  public getSnapshotArr(): T[] {
    return Array.from(this.subjectMap.getValue().values());
  }

  public getById(k: K): Observable<T> {
    return this.getMap().pipe(
      map(m => m.get(k)),
      filter(v => v != null),
    )
  }

  public getSnapshotById(k: K): T {
    return this.subjectMap.getValue().get(k)
  }

  public getByLateId(lateKey: Observable<K>): Observable<T> {
    return lateKey.pipe(flatMap((k) => this.getMap().pipe(
      map(m => m.get(k),
        filter(v => v != null),
      ))));
  }

  public getByIds(ks: K[]): Observable<T[]> {
    return this.getMap().pipe(map(m => ks.map(k => m.get(k)).filter(v => v != null)))
  }

  /**
   * Get all that does NOT have these IDs
   */
  public getByIdsInverse(k: K[]): Observable<T[]> {
    return this.getMap().pipe(map(m => {
      let keys = Array.from(m.keys()).filter(a => !k.includes(a));
      return keys.map(key => m.get(key));
    }));
  }

  public isHealthy(): boolean {
    return this.wsp.isOpened;
  }

  public filter(predicate: (T) => boolean): Observable<T[]> {
    return this.getArr().pipe(filter((h) => predicate(h)));
  }

  private async fetchAllAsync(): Promise<void> {
    await this.http.get<T[]>(this.path)
      .forEach((all: T[]) => {
        all.forEach((t: T) => {
          let k = this.key(t);
          this.publish(k, t);
        });
      });
  }

  private async connectWsAsync(): Promise<void> {
    console.debug(`Connecting to websocket path ${this.path}`);
    try {
      if (this.wsp) {
        await this.wsp.close();
      }
    } catch (e) {
      console.error("WebSockets close failed", e);
    }
    try {
      await this.wsp.open();
      console.info("WebSockets connect success");
      await this.fetchAllAsync();
    } catch (e) {
      console.error("WebSockets connect failed", e);
    }
  }

  ngOnDestroy(): void {
    (async () => {
      await this.wsp.close();
    })();
  }
}
