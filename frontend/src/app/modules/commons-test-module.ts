import {NgModule} from "@angular/core";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MAT_BOTTOM_SHEET_DATA} from "@angular/material";
import {ActivatedRoute, Params} from "@angular/router";
import {Subject} from "rxjs";
import {CommonsModule} from "./commons-module";

let params = new Subject<Params>();
params.next({id: 1});

@NgModule({
  imports: [
    HttpClientTestingModule,
    NoopAnimationsModule,
    CommonsModule,
  ],
  providers: [
    {provide: MAT_BOTTOM_SHEET_DATA, useValue: {data: {text: 'hej'}}},
    {provide: ActivatedRoute, useValue: {params: params}}
  ],
})
export class CommonsTestModule {
}
