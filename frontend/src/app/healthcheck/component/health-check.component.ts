import {Component, Input} from '@angular/core';
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {Router} from "@angular/router";

@Component({
  selector: 'app-health-check',
  templateUrl: './health-check.component.html',
  styleUrls: ['./health-check.component.scss']
})
export class HealthCheckComponent {

  @Input() id: number;

  constructor(private service: HealthCheckService, private router: Router) {
  }

  getHealthcheck(): HealthCheckEndpoint {
    return this.service.keyedData.get(this.id)
  }

  getColor(): string {
    switch (this.getHealthcheck().status) {
      case null:
        return 'warn';
      case "HEALTHY":
        return 'primary';
      case "UNVERIFIED":
        return 'warn';
      case "UNHEALTHY":
        return 'warn';
      case "UNRESPONSIVE":
        return 'warn';
      case "UNSTABLE":
        return 'warn';
      case "UNKNOWN":
        return 'warn';
    }
  }

  async toHealthcheckPage(): Promise<void> {
    await this.router.navigate(["/healthcheck", this.id])
  }
}
