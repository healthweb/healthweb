import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {WarningBottomSheetComponent} from './modules/warning-bottom-sheet/component/warning-bottom-sheet.component';
import {CommonsModule} from "./modules/commons-module";

@NgModule({
  entryComponents: [
    WarningBottomSheetComponent,
  ],
  imports: [
    HttpClientModule,
    BrowserAnimationsModule,
    CommonsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
