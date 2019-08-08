import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WarningBottomSheetComponent} from './warning-bottom-sheet.component';

describe('WarningBottomSheetComponent', () => {
  let component: WarningBottomSheetComponent;
  let fixture: ComponentFixture<WarningBottomSheetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningBottomSheetComponent ]
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
