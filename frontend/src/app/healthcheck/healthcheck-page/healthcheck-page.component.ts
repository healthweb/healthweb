import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../../dashboard/dashboard-service/dashboard.service";
import {ActivatedRoute, Params} from "@angular/router";
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-healthcheck-page',
  templateUrl: './healthcheck-page.component.html',
  styleUrls: ['./healthcheck-page.component.scss']
})
export class HealthcheckPageComponent implements OnInit {

  private healthCheckId: number;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private healthService: HealthCheckService) {

  }

  getHealthCheck(): HealthCheckEndpoint {
    return this.healthService.keyedData.get(this.healthCheckId)
  }

  ngOnInit(): void {
    this.route.params.subscribe((p: Params) => {
      this.healthCheckId = +p["id"];
    });
  }
}
