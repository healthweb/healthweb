import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Dashboard} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {map, timeout} from 'rxjs/operators';
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DashboardService extends AbstractWebsocketModule <number, Dashboard> {

  constructor(http: HttpClient) {
    super("/dashboard", (d: Dashboard) => d.id, http)
  }

  public save(dashboard: Dashboard): Observable<Dashboard> {
    console.debug("Saving dashboard");
    return this.http.post("/dashboard", dashboard).pipe(
      timeout(1000),
      map((resp: Dashboard) => resp),
    )
  }

  public link(dashboardId: Number, healthCheckId: Number): Observable<any> {
    console.debug("Linking");
    return this.http.post(`/dashboard/link`, {
      'dashboardId': `${dashboardId}`,
      'healthCheckId': `${healthCheckId}`,
    }).pipe(
      timeout(1000),
    );
  }

  public unLink(dashboardId: Number, healthCheckId: Number): Observable<any> {
    return this.http.post(`/dashboard/unlink`, {
      'dashboardId': `${dashboardId}`,
      'healthCheckId': `${healthCheckId}`,
    }).pipe(
      timeout(1000),
    );
  }
}
