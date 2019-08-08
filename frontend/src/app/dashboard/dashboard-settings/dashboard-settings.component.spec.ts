import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardSettingsComponent} from './dashboard-settings.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('DashboardSettingsComponent', () => {
  let component: DashboardSettingsComponent;
  let fixture: ComponentFixture<DashboardSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
