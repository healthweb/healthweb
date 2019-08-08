import {NgModule} from "@angular/core";
import {AppRoutingModule} from "../app-routing.module";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MAT_BOTTOM_SHEET_DATA, MatBottomSheetModule} from "@angular/material";
import {BrowserModule} from "@angular/platform-browser";
import {MaterialModule} from "./material-module/material.module";
import {FormsModule} from "@angular/forms";
import {AppComponent} from "../app.component";
import {HealthCheckComponent} from "../healthcheck/component/health-check.component";
import {PageNotFoundComponent} from "../page-not-found/page-not-found.component";
import {DashboardListComponent} from "../dashboard/dashboard-list/dashboard-list.component";
import {DashboardPageComponent} from "../dashboard/dashboard-page/dashboard-page.component";
import {DashboardSettingsComponent} from "../dashboard/dashboard-settings/dashboard-settings.component";
import {HealthcheckPageComponent} from "../healthcheck/healthcheck-page/healthcheck-page.component";
import {HealthcheckListComponent} from "../healthcheck/healthcheck-list/healthcheck-list.component";
import {WarningBottomSheetComponent} from "../component/warning-bottom-sheet/warning-bottom-sheet.component";
import {ActivatedRoute, Params} from "@angular/router";
import {Subject} from "rxjs";

let params = new Subject<Params>();
params.next({id: 1});

@NgModule({
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    HttpClientTestingModule,
    MatBottomSheetModule,
    AppRoutingModule,
    MaterialModule,
    FormsModule,
  ],
  providers: [
    {provide: MAT_BOTTOM_SHEET_DATA, useValue: {data: {text: 'hej'}}},
    {provide: ActivatedRoute, useValue: {params: params}}
  ],
  declarations: [
    AppComponent,
    HealthCheckComponent,
    PageNotFoundComponent,
    DashboardListComponent,
    DashboardPageComponent,
    DashboardSettingsComponent,
    HealthcheckPageComponent,
    HealthcheckListComponent,
    WarningBottomSheetComponent,
  ],
})
export class CommonsTestModule {
}
