import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Dashboard} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DashboardService extends AbstractWebsocketModule <Dashboard> {

  constructor(private http: HttpClient) {
    super("/dashboard", (d: Dashboard) => d.id.toString())
  }

  public add(dashboard: Dashboard): Observable<Dashboard> {
    console.info("Adding new sashboard");
    return this.http.post("/dashboard", dashboard).pipe(
      map((resp: Dashboard) => resp),
    )
  }

  public archive(id: Number): Observable<Object> {
    return this.http.delete(`/dashboard/${id}`)
  }

  public link(dashboardId: Number, healthCheckId: Number):Observable<Object> {
    return this.http.put(`/dashboard/healthcheck/${dashboardId}/${healthCheckId}`, null)
  }

  public unLink(dashboardId: Number, healthCheckId: Number): Observable<Object> {
    return this.http.delete(`/dashboard/healthcheck/${dashboardId}/${healthCheckId}`)
  }
}
