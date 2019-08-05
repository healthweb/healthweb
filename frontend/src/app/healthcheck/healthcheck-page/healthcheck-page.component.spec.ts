import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HealthcheckPageComponent} from './healthcheck-page.component';

describe('HealthcheckPageComponent', () => {
  let component: HealthcheckPageComponent;
  let fixture: ComponentFixture<HealthcheckPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HealthcheckPageComponent ]
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
