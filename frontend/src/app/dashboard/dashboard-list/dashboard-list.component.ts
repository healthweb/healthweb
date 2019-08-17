import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {Router} from "@angular/router";
import {WarningBottomSheetService} from "../../modules/warning-bottom-sheet/service/warning-bottom-sheet.service";

@Component({
  selector: 'app-dashboard-list',
  templateUrl: './dashboard-list.component.html',
  styleUrls: ['./dashboard-list.component.scss']
})
export class DashboardListComponent implements OnInit {

  private addFormShow = false;
  private addButtonDisabled = false;

  private readonly displayedColumns = ['id', 'name', 'watched'];

  constructor(private dashboardService: DashboardService,
              private router: Router,
              private warningsService: WarningBottomSheetService) {
  }

  ngOnInit() {
  }

  public async saveNew(name:string) {
    try {
      this.addButtonDisabled = true;
      let d = await this.dashboardService.save({
        id: null,
        name: name,
        description: null,
        healthchecks: [],
        archived: false
      }).toPromise();
      console.info(`Saved new dashboard :: ${JSON.stringify(d, null, '\t')}`);
      this.addButtonDisabled = false;
      this.addFormShow = false;
      await this.router.navigate([`/dashboard/${d.id}`]);
    }catch (e) {
      this.warningsService.warning("Failed saving dashboard", e)
    }
  }
}
