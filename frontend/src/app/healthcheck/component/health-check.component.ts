import {Component, Input} from '@angular/core';
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";

@Component({
  selector: 'app-health-check',
  templateUrl: './health-check.component.html',
  styleUrls: ['./health-check.component.scss']
})
export class HealthCheckComponent {

  @Input() id: number;

  constructor(private service: HealthCheckService) {
  }

  getHealthcheck(): HealthCheckEndpoint {
    return this.service.keyedData.get(this.id)
  }
}
