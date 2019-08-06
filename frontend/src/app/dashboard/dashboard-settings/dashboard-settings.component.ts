import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";
import {Dashboard} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-dashboard-settings',
  templateUrl: './dashboard-settings.component.html',
  styleUrls: ['./dashboard-settings.component.scss']
})
export class DashboardSettingsComponent implements OnInit {

  private dashboardId: number;
  private newHosts: string;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private router: Router,
              private healthService: HealthCheckService) {

  }

  getDashboard(): Dashboard {
    return this.dashboardService.keyedData.get(this.dashboardId);
  }

  async saveHost() {
    let hc = this.healthService.getForUrl(this.newHosts);
    try {
      if (!hc) {
        hc = await this.healthService.saveNew(this.newHosts)
      }
      await this.dashboardService.link(+this.dashboardId, hc._id)
    } catch (e) {
      console.warn("Failed adding/linking new url", e)
    }
  }

  ngOnInit(): void {
    this.route.params.subscribe((p: Params) => {
      this.dashboardId = +p["id"];
    });
  }
}
