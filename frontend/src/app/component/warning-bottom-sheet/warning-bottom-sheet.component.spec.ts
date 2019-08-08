import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WarningBottomSheetComponent} from './warning-bottom-sheet.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('WarningBottomSheetComponent', () => {
  let component: WarningBottomSheetComponent;
  let fixture: ComponentFixture<WarningBottomSheetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningBottomSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
