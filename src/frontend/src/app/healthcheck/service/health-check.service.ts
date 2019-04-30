import {Injectable, OnDestroy} from '@angular/core';
// @ts-ignore
import WebSocketAsPromised from 'websocket-as-promised';
import {HealthCheck} from "../model/health-check";

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService implements OnDestroy {
  wsp = new WebSocketAsPromised(`ws://${window.location.hostname}:${window.location.port}/health`);

  constructor() {
    (async () =>{
      try {
        await this.wsp.open();
        this.wsp.send('message');
        this.wsp.onMessage.addListener(msg => {
          let hc: HealthCheck = JSON.parse(msg);
          console.log(hc.url);
        });
      } catch (e) {
        console.error(e);
      }
    })()
  }

  ngOnDestroy(): void {
    console.log("Destroying healthcheck service");
    (async () => {
      await this.wsp.close();
    })();
  }
}
