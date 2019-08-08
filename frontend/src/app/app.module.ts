import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HealthCheckComponent} from './healthcheck/component/health-check.component';
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {DashboardListComponent} from './dashboard/dashboard-list/dashboard-list.component';
import {DashboardPageComponent} from './dashboard/dashboard-page/dashboard-page.component';
import {MaterialModule} from "./modules/material-module/material.module";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {DashboardSettingsComponent} from './dashboard/dashboard-settings/dashboard-settings.component';
import {HealthcheckPageComponent} from './healthcheck/healthcheck-page/healthcheck-page.component';
import {HealthcheckListComponent} from './healthcheck/healthcheck-list/healthcheck-list.component';
import {MatBottomSheetModule} from "@angular/material";
import {WarningBottomSheetComponent} from './component/warning-bottom-sheet/warning-bottom-sheet.component';

@NgModule({
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
  entryComponents: [
    WarningBottomSheetComponent,
  ],
  imports: [
    MatBottomSheetModule,
    BrowserModule,
    HttpClientModule, // import HttpClientModule after BrowserModule.
    AppRoutingModule,
    MaterialModule,
    BrowserAnimationsModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
