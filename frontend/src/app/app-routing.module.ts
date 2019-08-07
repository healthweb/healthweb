import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {DashboardListComponent} from "./dashboard/dashboard-list/dashboard-list.component";
import {DashboardPageComponent} from "./dashboard/dashboard-page/dashboard-page.component";
import {DashboardSettingsComponent} from "./dashboard/dashboard-settings/dashboard-settings.component";
import {HealthcheckPageComponent} from "./healthcheck/healthcheck-page/healthcheck-page.component";
import {HealthcheckListComponent} from "./healthcheck/healthcheck-list/healthcheck-list.component";

const routes: Routes = [
    {
      path: '',
      component: DashboardListComponent,
      pathMatch: 'full',
    },
    {
      path: 'dashboard',
      children: [
        {
          path: ':id',
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
          path: '',
          redirectTo: '/',
          pathMatch: 'full',
        },
      ],
    },

    {
      path: 'healthcheck',
      children: [
        {
          path: ':id',
          component: HealthcheckPageComponent,
        },
        {
          path: '',
          component:HealthcheckListComponent,
          pathMatch: 'full',
        },
      ],
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
