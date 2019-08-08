import {Component} from '@angular/core';
import {DashboardService} from "../../dashboard/dashboard-service/dashboard.service";
import {ActivatedRoute} from "@angular/router";
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {Observable} from "rxjs";
import {flatMap, map} from "rxjs/operators";

@Component({
  selector: 'app-healthcheck-page',
  templateUrl: './healthcheck-page.component.html',
  styleUrls: ['./healthcheck-page.component.scss']
})
export class HealthcheckPageComponent {

  private id: Observable<number>;
  private hc: Observable<HealthCheckEndpoint>;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private healthService: HealthCheckService) {

    this.id = this.route.params.pipe(map(p => +p["id"]));
    this.hc = this.id.pipe(flatMap(id => this.healthService.getById(id)));
  }
}
