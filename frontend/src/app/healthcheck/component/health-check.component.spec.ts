import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HealthCheckComponent} from './health-check.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('HealthCheckComponent', () => {
  let component: HealthCheckComponent;
  let fixture: ComponentFixture<HealthCheckComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
