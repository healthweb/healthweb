import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute, Router} from "@angular/router";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";
import {Dashboard} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-dashboard-settings',
  templateUrl: './dashboard-settings.component.html',
  styleUrls: ['./dashboard-settings.component.scss']
})
export class DashboardSettingsComponent {

  private dashboardId: number;

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private router: Router,
              private healthService: HealthCheckService) {
    (async () => {
      let p = await this.route.params.toPromise();
      if (dashboardService.keyedData[p.id] == null) {
        console.warn(`Cant find that dashboard with id=${p.id}`);
        await router.navigate(["/"]);
      } else {
        this.dashboardId = p.id;
      }
    })();
  }

  getDashboard(): Dashboard {
    return this.dashboardService.keyedData.get(this.dashboardId.toString())
  }

}
