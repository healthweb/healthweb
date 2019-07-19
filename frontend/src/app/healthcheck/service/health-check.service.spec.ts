import { TestBed } from '@angular/core/testing';

import { HealthCheckService } from './health-check.service';

describe('HealthCheckService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HealthCheckService = TestBed.get(HealthCheckService);
    expect(service).toBeTruthy();
  });
});
