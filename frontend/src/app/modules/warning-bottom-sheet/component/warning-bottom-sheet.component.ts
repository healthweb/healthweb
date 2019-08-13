import {Component, Inject} from '@angular/core';
import {MAT_BOTTOM_SHEET_DATA} from "@angular/material";
import {WarningMessage} from "../warning-message-model";

@Component({
  selector: 'app-warning-bottom-sheet',
  templateUrl: './warning-bottom-sheet.component.html',
  styleUrls: ['./warning-bottom-sheet.component.scss']
})
export class WarningBottomSheetComponent {

  private messages: Map<number, WarningMessage>;

  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) public data: any) {
    this.messages = data.messages;
  }
}
