import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import {DashboardListComponent} from "./dashboard/dashboard-list/dashboard-list.component";
import {DashboardPageComponent} from "./dashboard/dashboard-page/dashboard-page.component";

const routes: Routes = [
    {
      path: '',
      component: DashboardListComponent,
      pathMatch: 'full'
    },
    {
      path: 'dashboard/:dashboardId',
      component: DashboardPageComponent,
    },
    {
      path: '**',
      component: PageNotFoundComponent,
    }
  ]
;

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
