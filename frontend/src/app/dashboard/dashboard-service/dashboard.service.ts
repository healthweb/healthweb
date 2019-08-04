import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Dashboard} from '../../../shared/healthweb-shared'
import {AbstractWebsocketModule} from "../../modules/abstract/abstract-websocket/abstract-websocket.module";
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DashboardService extends AbstractWebsocketModule <Dashboard> {

  constructor(private http: HttpClient) {
    super("/dashboard", (d: Dashboard) => d.id.toString())

  }

  public async add(dashboard: Dashboard): Promise<Dashboard> {
    console.info("Adding new sashboard");
    return await this.http.post("/dashboard", dashboard).pipe(
      map((resp: Dashboard) => resp),
    ).toPromise()
  }

  public async archive(id: Number): Promise<any> {
    return await this.http.delete(`/dashboard/${id}`).toPromise()
  }

  public async link(dashboardId: Number, healthCheckId: Number): Promise<any> {
    return await this.http.post(`/dashboard/link`, {
      'dashboardId': `${dashboardId}`,
      'healthCheckId': `${healthCheckId}`,
    }).toPromise()
  }

  public async unLink(dashboardId: Number, healthCheckId: Number): Promise<any> {
    return await this.http.post(`/dashboard/unlink`, {
      'dashboardId': `${dashboardId}`,
      'healthCheckId': `${healthCheckId}`,
    }).toPromise()
  }
}
