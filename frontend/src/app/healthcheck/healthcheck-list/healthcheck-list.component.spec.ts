import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HealthcheckListComponent} from './healthcheck-list.component';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('HealthcheckListComponent', () => {
  let component: HealthcheckListComponent;
  let fixture: ComponentFixture<HealthcheckListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [CommonsTestModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthcheckListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
