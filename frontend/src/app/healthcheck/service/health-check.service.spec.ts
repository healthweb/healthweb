import {TestBed} from '@angular/core/testing';

import {HealthCheckService} from './health-check.service';
import {CommonsTestModule} from "../../modules/commons-test-module";

describe('HealthCheckService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      CommonsTestModule,
    ],
  }));

  it('should be created', () => {
    const service: HealthCheckService = TestBed.get(HealthCheckService);
    expect(service).toBeTruthy();
  });
});
