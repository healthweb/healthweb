import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Params} from "@angular/router";
import {Dashboard, HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit {

  private dashboardId: number;
  private readonly displayedColumns: string[] = ['id', 'status', 'url'];

  constructor(private dashboardService: DashboardService,
              private healthcheckService: HealthCheckService,
              private route: ActivatedRoute) {

  }

  getDashboard(): Dashboard {
    return this.dashboardService.keyedData.get(this.dashboardId)
  }

  getHealthchecks(): HealthCheckEndpoint[] {
    return this.getDashboard().healthchecks
      .map((id: number) => this.healthcheckService.keyedData.get(id))
  }

  ngOnInit(): void {
    this.route.params.subscribe((p: Params) => {
      this.dashboardId = +p["id"];
    });
  }
}
