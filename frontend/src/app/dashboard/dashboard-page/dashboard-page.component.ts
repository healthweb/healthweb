import {Component} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {ActivatedRoute} from "@angular/router";
import {Dashboard, HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {HealthCheckService} from "../../healthcheck/service/health-check.service";
import {Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {flatMap} from "rxjs/internal/operators";
import {WarningBottomSheetService} from "../../modules/warning-bottom-sheet/service/warning-bottom-sheet.service";

@Component({
  selector: 'app-dashboard-page',
  templateUrl: './dashboard-page.component.html',
  styleUrls: ['./dashboard-page.component.scss']
})
export class DashboardPageComponent {

  private readonly selection: { [key: number]: boolean } = {};
  private readonly displayedColumns: string[] = ['id', 'status', 'url', 'select'];
  private readonly dashboard: Observable<Dashboard>;
  private readonly healthChecks: Observable<HealthCheckEndpoint[]>;

  private id: number;

  constructor(private dashboardService: DashboardService,
              private healthcheckService: HealthCheckService,
              private route: ActivatedRoute,
              private warningService: WarningBottomSheetService,
  ) {

    let id = this.route.params.pipe(
      map(p => +p["id"]),
      tap(i => this.id = i),
    );
    this.dashboard = id.pipe(flatMap(id => this.dashboardService.getById(id)));
    this.healthChecks = this.dashboard.pipe(flatMap(d => this.healthcheckService.getByIds(d.healthchecks)))
  }

  private trunkString(s: string) {
    if (s && s.length > 50) {
      return `${s.slice(0, 47)}...`;
    } else {
      return s;
    }
  }

  private anySelected(): boolean {
    return Object.values(this.selection).some(v => v);
  }

  private toggleSelect(id:number){
    if(this.selection[id] == undefined){
      this.selection[id] = true;
    }else{
      this.selection[id] = !this.selection[id];
    }
  }

  private statusClass(status:string):string{
    if(status === 'HEALTHY'){
      return undefined;
    }else{
      return 'row_warn';
    }
  }

  private async deleteSelected(): Promise<void> {
    try {
      let promisses: Promise<any>[] = Object.keys(this.selection)
        .filter(k => this.selection[k])
        .map(k => +k)
        .map(async (k: number) => {
          delete this.selection[k];
          await this.dashboardService.unLink(this.id, k).toPromise();
        });
      await Promise.all(promisses);
    } catch (e) {
      this.warningService.warning("Failed unlinking one or all selected health checks.", e)
    }
  }
}
