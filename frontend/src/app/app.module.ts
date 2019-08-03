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

@NgModule({
  declarations: [
    AppComponent,
    HealthCheckComponent,
    PageNotFoundComponent,
    DashboardListComponent,
    DashboardPageComponent,
  ],
  imports: [
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
