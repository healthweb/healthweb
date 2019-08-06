import {Injectable} from '@angular/core';
import {HealthCheckEndpoint} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService extends AbstractWebsocketModule<number, HealthCheckEndpoint> {

  constructor(http: HttpClient) {
    super("/health", (hc: HealthCheckEndpoint) => hc._id, http);
  }

  async saveNew(url: string): Promise<HealthCheckEndpoint> {
    console.info(`saving new host with url ${url}`);//TODO
    return await this.http.post("/health", {'url': `'${url}'`})
      .pipe(map((hc: HealthCheckEndpoint) => hc)).toPromise()
  }

  getForUrl(url:string):HealthCheckEndpoint{
    return this.data.filter((h) => h.url == url)[0];
  }
}
