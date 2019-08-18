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
import {
  MAT_DIALOG_DEFAULT_OPTIONS,
  MatBadgeModule,
  MatBottomSheetModule,
  MatChipsModule,
  MatListModule,
  MatTooltipModule
} from "@angular/material";
import {AppRoutingModule} from "../app-routing.module";
import {MaterialModule} from "./material-module/material.module";
import {FormsModule} from "@angular/forms";
import {DialogComponent} from "../dialog/dialog.component";

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
  DialogComponent,
];
let modules: any[] = [
  MatBottomSheetModule,
  BrowserModule,
  AppRoutingModule,
  MaterialModule,
  FormsModule,
  MatListModule,
  MatTooltipModule,
  MatChipsModule,
  MatBadgeModule,
];

@NgModule({
  imports: modules,
  declarations: components,
  providers: [
    {provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: {hasBackdrop: true}}
  ],
  exports: modules,
})
export class CommonsModule {
}
