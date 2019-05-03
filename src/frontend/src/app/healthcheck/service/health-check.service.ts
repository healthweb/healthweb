import {Injectable, OnDestroy} from '@angular/core';
// @ts-ignore
import WebSocketAsPromised from 'websocket-as-promised';
import {HealthCheckEndpoint} from '../../../shared/shared-types'

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
          let hc: HealthCheckEndpoint = JSON.parse(msg);
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
