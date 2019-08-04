import {Injectable} from '@angular/core';
import {HealthCheckEndpoint} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService extends AbstractWebsocketModule<HealthCheckEndpoint> {

  constructor(private http: HttpClient) {
    super("/health", (hc: HealthCheckEndpoint) => hc._id.toString());
  }

  saveNew(url: string): Observable<HealthCheckEndpoint> {
    console.info(`saving new host with url ${url}`);//TODO
    return this.http.post("/health", {'url': `'${url}'`})
      .pipe(map((hc: HealthCheckEndpoint) => hc))
  }
}
