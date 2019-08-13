import {Injectable} from '@angular/core';
import {MatBottomSheet} from "@angular/material";
import {WarningBottomSheetComponent} from "../component/warning-bottom-sheet.component";
import {WarningMessage} from "../warning-message-model";

@Injectable({
  providedIn: 'root'
})
export class WarningBottomSheetService {

  private idTicker: number = 0;
  private warnings = new Map<number, WarningMessage>();

  constructor(private bottom: MatBottomSheet) {
  }

  public warning(text: string, err:any): WarningMessage {
    if(err){
      console.warn(text, err);
      text = text+" \nCheck dev console for more info..";
    }
    let id: number = ++this.idTicker;
    let msg: WarningMessage = {
      text: text, dismissCallback: () => {
        try {
          this.warnings.delete(id);
          if(this.warnings.size == 0){
            this.bottom.dismiss();
          }
        } catch (e) {
          this.warning("Close callback failed.", e);
        }
      }
    };
    this.warnings.set(id, msg);
    this.bottom.open(WarningBottomSheetComponent, {data: {messages: this.warnings}});
    return msg;
  }
}
