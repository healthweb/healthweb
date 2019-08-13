import {TestBed} from '@angular/core/testing';

import {WarningBottomSheetService} from './warning-bottom-sheet.service';
import {MatBottomSheetModule} from "@angular/material";

describe('WarningBottomSheetService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      MatBottomSheetModule
    ],
  }));

  it('should be created', () => {
    const service: WarningBottomSheetService = TestBed.get(WarningBottomSheetService);
    expect(service).toBeTruthy();
  });
});
