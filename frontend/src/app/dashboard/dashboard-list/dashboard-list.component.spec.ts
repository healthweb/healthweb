import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardListComponent} from './dashboard-list.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('DashboardListComponent', () => {
  let component: DashboardListComponent;
  let fixture: ComponentFixture<DashboardListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
