import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HealthcheckPageComponent} from './healthcheck-page.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('HealthcheckPageComponent', () => {
  let component: HealthcheckPageComponent;
  let fixture: ComponentFixture<HealthcheckPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthcheckPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
