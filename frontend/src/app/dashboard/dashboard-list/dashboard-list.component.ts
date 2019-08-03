import {Component, OnInit} from '@angular/core';
import {DashboardService} from "../dashboard-service/dashboard.service";
import {Router} from "@angular/router";
import {Dashboard} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-dashboard-list',
  templateUrl: './dashboard-list.component.html',
  styleUrls: ['./dashboard-list.component.scss']
})
export class DashboardListComponent implements OnInit {

  addButtonDisabled = false;
  public addName: string = "";
  public addDescription: string = "";

  constructor(private dashboardService: DashboardService, private router: Router) {
  }

  ngOnInit() {
  }

  public async saveNew() {
    this.addButtonDisabled = true;
    this.dashboardService.add({
      id: null,
      name: this.addName,
      description: this.addDescription,
      healthchecks: [],
      archived: false
    }).subscribe(
      (d: Dashboard) => {
        console.info(`Saved new dashboard :: ${JSON.stringify(d, null, '\t')}`);
        this.addButtonDisabled = false;
        this.router.navigate([`/dashboard/${d.id}`]);
      },
      (err) => {
        console.error("Failed saving new dashboard", err);
        this.addButtonDisabled = false;
      }
    );
  }

}
