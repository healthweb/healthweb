import {NgModule} from "@angular/core";
import {AppComponent} from "../app.component";
import {HealthCheckComponent} from "../healthcheck/component/health-check.component";
import {PageNotFoundComponent} from "../page-not-found/page-not-found.component";
import {DashboardListComponent} from "../dashboard/dashboard-list/dashboard-list.component";
import {DashboardPageComponent} from "../dashboard/dashboard-page/dashboard-page.component";
import {DashboardSettingsComponent} from "../dashboard/dashboard-settings/dashboard-settings.component";
import {HealthcheckPageComponent} from "../healthcheck/healthcheck-page/healthcheck-page.component";
import {HealthcheckListComponent} from "../healthcheck/healthcheck-list/healthcheck-list.component";
import {WarningBottomSheetComponent} from "./warning-bottom-sheet/component/warning-bottom-sheet.component";
import {BrowserModule} from "@angular/platform-browser";
import {MatBottomSheetModule, MatListModule} from "@angular/material";
import {AppRoutingModule} from "../app-routing.module";
import {MaterialModule} from "./material-module/material.module";
import {FormsModule} from "@angular/forms";

let components: any[] = [
  AppComponent,
  HealthCheckComponent,
  PageNotFoundComponent,
  DashboardListComponent,
  DashboardPageComponent,
  DashboardSettingsComponent,
  HealthcheckPageComponent,
  HealthcheckListComponent,
  WarningBottomSheetComponent,
];
let modules: any[] = [
  MatBottomSheetModule,
  BrowserModule,
  AppRoutingModule,
  MaterialModule,
  FormsModule,
  MatListModule,
];

@NgModule({
  imports: modules,
  declarations: components,
  exports: modules,
})
export class CommonsModule {}
