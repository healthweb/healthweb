import {Injectable} from '@angular/core';
import {HealthCheckEndpoint} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {HttpClient} from "@angular/common/http";
import {timeout} from "rxjs/operators";
import {Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService extends AbstractWebsocketModule<number, HealthCheckEndpoint> {

  constructor(http: HttpClient) {
    super("/health", (hc: HealthCheckEndpoint) => hc.id, http);
  }

  public saveNew(url: string): Observable<HealthCheckEndpoint> {
    let hcs = this.getSnapshotArr().filter(h => h.url === url);
    return hcs[0] ? of(hcs[0]) : this.postNew(url);
  }

  private postNew(url: string): Observable<HealthCheckEndpoint> {
    return this.http.post<HealthCheckEndpoint>("/health", {'url': `${url}`}).pipe(timeout(1_000));
  }
}
