import {Component} from '@angular/core';
import {DashboardService} from "../../dashboard/dashboard-service/dashboard.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-healthcheck-page',
  templateUrl: './healthcheck-page.component.html',
  styleUrls: ['./healthcheck-page.component.scss']
})
export class HealthcheckPageComponent {

  private healthCheckId: number;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private router: Router,
              private healthService: HealthCheckService) {

    this.route.params.subscribe((p: Params) => {
      this.healthCheckId = p["id"];
      if (healthService.get(p["id"]) == null) {
        console.warn(`Could not find healthcheck! ${JSON.stringify(Array.from(healthService.keyedData.keys()))}`);
        router.navigate(["/"])
      }
    }, (err) => {
      console.error("Error", err);
      router.navigate(["/"]);
    });
  }

  getHealthCheck():HealthCheckEndpoint{
    return this.healthService.get(`${this.healthCheckId}`)
  }
}
