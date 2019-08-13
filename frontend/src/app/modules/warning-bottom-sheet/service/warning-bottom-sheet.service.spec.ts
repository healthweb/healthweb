import {TestBed} from '@angular/core/testing';

import {WarningBottomSheetService} from './warning-bottom-sheet.service';

describe('WarningBottomSheetService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WarningBottomSheetService = TestBed.get(WarningBottomSheetService);
    expect(service).toBeTruthy();
  });
});
