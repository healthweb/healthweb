import {NgModule, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
// @ts-ignore
import WebSocketAsPromised from "websocket-as-promised";

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ]
})
export abstract class AbstractWebsocketModule<T> implements OnDestroy {

  private readonly wsp: WebSocketAsPromised;
  keyedData: Map<string, T> = new Map<string, T>();
  data: T[] = [];

  protected constructor(private path: string, private key: (T) => string) {
    let wsUrl = `${window.location.hostname}:${window.location.port}${path}`;
    if (location.protocol == 'https:') {
      this.wsp = new WebSocketAsPromised(`wss://${wsUrl}`);
    } else {
      this.wsp = new WebSocketAsPromised(`ws://${wsUrl}`);
    }
    this.wsp.onMessage.addListener(msg => {
      console.info(`We got datas ${msg}`)//TODO
      let hc: T = JSON.parse(msg);
      this.keyedData.set(this.key(hc), hc);
      this.data = Array.from(this.keyedData.values());
    });
    this.wsp.onClose.addListener(() => {
      console.warn("WebSockets disconnected");
      setTimeout(() => {
        console.info("WebSockets reconnecting");
        this.connectWS();
      }, 5_000)
    });
    (async () => {
      this.connectWS()
    })()
  }

  isHealthy(): boolean {
    return this.wsp.isOpened;
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
      this.data = [];
      this.keyedData.clear();
      console.info("WebSockets connect success");
    } catch (e) {
      console.error("WebSockets connect failed", e);
    }
  }

  ngOnDestroy(): void {
    (async () => {
      await this.wsp.close();
    })();
  }
}
