import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute} from "@angular/router";
import {Dashboard} from "../../../shared/healthweb-shared";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent {

  private dashboardId: number;
  private newHosts: string;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private healthService: HealthCheckService) {
    this.route.params.subscribe((p) => {
      console.log(JSON.stringify(p, null, '\t'));
      this.dashboardId = p.dashboardId;

    });
  }

  getDashboard(): Dashboard {
    return this.dashboardService.keyedData.get(this.dashboardId.toString())
  }

  async saveHost() {
    let hc = this.healthService.keyedData[this.newHosts];
    try {
      if (!hc) {
        hc = await this.healthService.saveNew(this.newHosts).toPromise()
      }
      await this.dashboardService.link(this.dashboardId, hc._id)
    } catch (e) {
      console.warn("Failed adding/linking new url", e)
    }
  }
}
