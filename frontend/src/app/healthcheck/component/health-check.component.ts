import {Component, Input} from '@angular/core';
import {HealthCheckService} from "../service/health-check.service";
import {HealthCheckEndpoint} from "../../../shared/healthweb-shared";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-health-check',
  templateUrl: './health-check.component.html',
  styleUrls: ['./health-check.component.scss']
})
export class HealthCheckComponent {

  @Input() id: number;
  private hc: Observable<HealthCheckEndpoint>;

  constructor(private service: HealthCheckService, private router: Router) {
    this.hc = service.getById(this.id)
  }

  private getColor(): Observable<string> {
    return this.hc.pipe(map(h => {
      if (h.status === "HEALTHY") {
        return 'primary';
      } else {
        return 'warn';
      }
    }));
  }

  async toHealthcheckPage(): Promise<void> {
    await this.router.navigate(["/healthcheck", this.id])
  }
}
