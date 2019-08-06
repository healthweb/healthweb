import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Params} from "@angular/router";
import {Dashboard} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent implements OnInit {

  private dashboardId: number;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute) {

  }

  getDashboard(): Dashboard {
    return this.dashboardService.keyedData.get(this.dashboardId)
  }

  ngOnInit(): void {
    this.route.params.subscribe((p: Params) => {
      this.dashboardId = +p["id"];
    });
  }
}
