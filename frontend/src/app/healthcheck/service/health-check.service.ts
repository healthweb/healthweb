import {Injectable} from '@angular/core';
import {HealthCheckEndpoint} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {HttpClient} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService extends AbstractWebsocketModule<HealthCheckEndpoint> {

  constructor(private http: HttpClient) {
    super("/health", (hc: HealthCheckEndpoint) => hc._id.toString());
  }

  async saveNew(url: string): Promise<HealthCheckEndpoint> {
    console.info(`saving new host with url ${url}`);//TODO
    return await this.http.post("/health", {'url': `'${url}'`})
      .pipe(map((hc: HealthCheckEndpoint) => hc)).toPromise()
  }

  getAll(): HealthCheckEndpoint[] {
    return Array.from(this.keyedData.values());
  }
}
