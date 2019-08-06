import {NgModule, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
// @ts-ignore
import WebSocketAsPromised from "websocket-as-promised";
import {HttpClient} from "@angular/common/http";

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export abstract class AbstractWebsocketModule<K, T> implements OnDestroy {

  private readonly wsp: WebSocketAsPromised;

  public readonly keyedData: Map<K, T> = new Map<K, T>();
  public readonly data: T[] = [];

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
      let isOld: boolean = this.keyedData.has(k);
      this.keyedData.set(k, t);
      if (isOld) {
        this.data.splice(0, this.data.length);
      }
      this.data.push.apply(this.data, Array.from(this.keyedData.values()));
    });
    this.wsp.onClose.addListener(() => {
      console.warn("WebSockets disconnected");
      setTimeout(() => {
        console.info("WebSockets reconnecting");
        this.connectWsAsync();
      }, 5_000)
    });
    this.connectWsAsync();
  }

  isHealthy(): boolean {
    return this.wsp.isOpened;
  }

  private async fetchAllAsync(): Promise<void> {
    this.http.get<T[]>(this.path)
      .forEach((all: T[]) => {
        this.data.splice(0, this.data.length);
        this.data.push.apply(this.data, all);
        all.forEach((t: T) => this.keyedData.set(this.key(t), t));
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
