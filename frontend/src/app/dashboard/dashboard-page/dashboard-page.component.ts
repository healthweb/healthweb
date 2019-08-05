import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Dashboard} from "../../../shared/healthweb-shared";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent {

  private dashboardId: string;
  private newHosts: string;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private router: Router,
              private healthService: HealthCheckService) {

    this.route.params.subscribe((p: Params) => {
      this.dashboardId = p["id"];
      if (dashboardService.get(p["id"]) == null) {
        console.warn(`Could not find dashboard! ${JSON.stringify(Array.from(dashboardService.keyedData.keys()))}`);
        router.navigate(["/"])
      }
    }, (err) => {
      console.error("Error", err);
      router.navigate(["/"]);
    });
  }

  getDashboard(): Dashboard {
    return this.dashboardService.get(this.dashboardId)
  }

  async saveHost() {
    let hc = this.healthService.keyedData[this.newHosts];
    try {
      if (!hc) {
        hc = await this.healthService.saveNew(this.newHosts)
      }
      await this.dashboardService.link(+this.dashboardId, hc._id)
    } catch (e) {
      console.warn("Failed adding/linking new url", e)
    }
  }
}
