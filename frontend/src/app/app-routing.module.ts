import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {DashboardListComponent} from "./dashboard/dashboard-list/dashboard-list.component";
import {DashboardPageComponent} from "./dashboard/dashboard-page/dashboard-page.component";
import {DashboardSettingsComponent} from "./dashboard/dashboard-settings/dashboard-settings.component";
import {HealthcheckPageComponent} from "./healthcheck/healthcheck-page/healthcheck-page.component";

const routes: Routes = [
    {
      path: '',
      component: DashboardListComponent,
      pathMatch: 'full',
    },
    {
      path: 'dashboard/:id',
      children: [
        {
          path: 'settings',
          component: DashboardSettingsComponent,
        },
        {
          path: '',
          component: DashboardPageComponent,
        },
      ],
    },
    {
      path: 'healthcheck/:id',
      component: HealthcheckPageComponent,
    },
    {
      path: '**',
      component: PageNotFoundComponent,
    },
  ]
;

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
