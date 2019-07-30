import {Injectable, OnDestroy} from '@angular/core';
// @ts-ignore
import WebSocketAsPromised from 'websocket-as-promised';
import {HealthCheckEndpoint} from '../../../shared/healthweb-shared'

@Injectable({
  providedIn: 'root'
})
export class HealthCheckService implements OnDestroy {

  wsp: WebSocketAsPromised;

  constructor() {
    let wsUrl = `${window.location.hostname}:${window.location.port}/health`;
    if (location.protocol == 'https:') {
      this.wsp = new WebSocketAsPromised(`wss://${wsUrl}`);
    } else {
      this.wsp = new WebSocketAsPromised(`ws://${wsUrl}`);
    }
    (async () => {
      this.connectWS()
    })()
  }

  private async connectWS() {
    try {
      if (this.wsp) {
        await this.wsp.close();
      }
    } catch (e) {
      console.error("WebSockets close failed", e);
    }
    try {
      await this.wsp.open();
      console.info("WebSockets connect success");
    } catch (e) {
      console.error("WebSockets connect failed", e);
      setTimeout(() => {
        console.info("WebSockets reconnecting");
        this.connectWS();
      }, 15_000)
    }
    this.wsp.onMessage.addListener(msg => {
      let hc: HealthCheckEndpoint = JSON.parse(msg);
      console.log(hc.url);
    });
    this.wsp.onClose.addListener(() => {
      console.warn("WebSockets disconnected");
      setTimeout(() => {
        console.info("WebSockets reconnecting");
        this.connectWS();
      }, 5_000)
    });
  }

  ngOnDestroy(): void {
    console.info("Destroying healthcheck service");
    (async () => {
      await this.wsp.close();
    })();
  }
}
