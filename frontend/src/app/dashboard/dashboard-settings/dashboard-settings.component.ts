import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";
import {Dashboard, HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {Observable} from "rxjs";
import {flatMap, map, tap} from "rxjs/operators";

@Component({
  selector: 'app-dashboard-settings',
  templateUrl: './dashboard-settings.component.html',
  styleUrls: ['./dashboard-settings.component.scss']
})
export class DashboardSettingsComponent {

  private newHosts: string;
  private readonly displayedColumns: string[] = ['id', 'status', 'url', 'add'];

  private readonly dashboard: Observable<Dashboard>;
  private readonly unwatchedHealthChecks: Observable<HealthCheckEndpoint[]>;
  private id: number;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private router: Router,
              private healthService: HealthCheckService) {

    let id = this.route.params.pipe(
      map(p => +p["id"]),
      tap(i => this.id = i),
    );
    this.dashboard = id.pipe(flatMap(id => this.dashboardService.getById(id)));
    this.unwatchedHealthChecks = this.dashboard.pipe(flatMap((d: Dashboard) => this.healthService.getByIdsInverse(d.healthchecks)));
  }

  private async saveHost(url: string): Promise<void> {
    let hc = await this.healthService.saveNew(url).toPromise();
    await this.linkHost(hc._id);
  }

  private async linkHost(hcId:number):Promise<void>{
    await this.dashboardService.link(this.id, hcId).toPromise();
  }
}
