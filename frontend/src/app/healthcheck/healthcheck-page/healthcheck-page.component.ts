import {Component} from '@angular/core';
import {DashboardService} from "../../dashboard/dashboard-service/dashboard.service";
import {ActivatedRoute} from "@angular/router";
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint, HealthCheckError, HealthChecks} from "../../../shared/healthweb-shared";
import {Observable} from "rxjs";
import {first, flatMap, map} from "rxjs/operators";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatChipInputEvent, MatDialog, MatTableDataSource} from "@angular/material";
import {WarningBottomSheetService} from "../../modules/warning-bottom-sheet/service/warning-bottom-sheet.service";
import {DialogComponent} from "../../dialog/dialog.component";

@Component({
  selector: 'app-healthcheck-page',
  templateUrl: './healthcheck-page.component.html',
  styleUrls: ['./healthcheck-page.component.scss']
})
export class HealthcheckPageComponent {

  private readonly id: Observable<number>;
  private readonly hc: Observable<HealthCheckEndpoint>;
  private readonly separatorKeysCodes: number[] = [ENTER, COMMA];

  private readonly displayedColumns = ['name', 'error', 'healthy', 'message',];
  private readonly datasource = new MatTableDataSource<TableHealthCheck>([]);

  constructor(private dashboardService: DashboardService,
              private route: ActivatedRoute,
              private healthService: HealthCheckService,
              private warningService: WarningBottomSheetService,
              private dialogService: MatDialog) {

    this.id = this.route.params.pipe(map(p => +p["id"]));
    this.hc = this.route.params.pipe(flatMap(p => this.healthService.getById(+p["id"])));
    this.hc.subscribe(h => this.datasource.data = this.transformToTable(h.lastResponse))
  }

  private transformToTable(hcs: HealthChecks): TableHealthCheck[] {
    return Object.keys(hcs.checks).map(k => {
      return {
        name: k,
        error: hcs.checks[k].error,
        healthy: hcs.checks[k].healthy,
        message: hcs.checks[k].message,
      }
    });
  }

  private showErr(hc: TableHealthCheck) {
    if (hc.error) {
      let show = hc.error.message;
      if (hc.error.stack) {
        show += '\nStacktrace:\n' + hc.error.stack.join("\n")
      }
      this.dialogService.open(DialogComponent, {data: {header: hc.name, text: show}});
    }
  }

  private async add(event: MatChipInputEvent) {
    if (!event.value || event.value.trim() === '') {
      return;
    }
    let tag = event.value.trim();
    if (!tag.match("^[0-9a-z_-]+$")) {
      this.warningService.warning("Invalid characters", null);
      return;
    }
    try {
      let sync = await this.hc.pipe(first()).toPromise();
      await this.healthService.tag(sync.id, tag).toPromise();
      event.input.value = "";
    } catch (e) {
      this.warningService.warning(`Failed tagging with '${tag}'`, e);
    }
  }

  private async remove(t: string) {
    try {
      let sync = await this.hc.pipe(first()).toPromise();
      await this.healthService.untag(sync.id, t).toPromise();
    } catch (e) {
      this.warningService.warning(`Failed removing tag '${t}'`, e);
    }
  }
}

interface TableHealthCheck {
  name: string;
  error: HealthCheckError | undefined;
  healthy: boolean;
  message: string;
}
