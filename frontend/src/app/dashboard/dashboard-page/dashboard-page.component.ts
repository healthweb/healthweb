import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute} from "@angular/router";
import {Dashboard, HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {flatMap} from "rxjs/internal/operators";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent {

  private readonly displayedColumns: string[] = ['id', 'status', 'url'];
  private readonly id: Observable<number>;
  private readonly dashboard: Observable<Dashboard>;
  private readonly healthChecks: Observable<HealthCheckEndpoint[]>;

  constructor(private dashboardService: DashboardService,
              private healthcheckService: HealthCheckService,
              private route: ActivatedRoute) {

    this.id = this.route.params.pipe(map(p => +p["id"]));
    this.dashboard = this.id.pipe(flatMap(id => this.dashboardService.getById(id)));
    this.healthChecks = this.dashboard.pipe(flatMap(d => this.healthcheckService.getByIds(d.healthchecks)))
  }

  private trunkString(s: string) {
    if (s && s.length > 50) {
      return `${s.slice(0, 47)}...`;
    } else {
      return s;
    }
  }
}
