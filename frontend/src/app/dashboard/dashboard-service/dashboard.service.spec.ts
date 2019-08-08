import {TestBed} from '@angular/core/testing';

import {DashboardService} from './dashboard.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DashboardService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule],
  }));

  it('should be created', () => {
    const service: DashboardService = TestBed.get(DashboardService);
    expect(service).toBeTruthy();
  });
});
