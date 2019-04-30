import {HealthCheck} from './health-check';

describe('HealthCheck', () => {
  it('should create an instance', () => {
    let hc: HealthCheck = {url: "localhost"};
    expect(hc).toBeTruthy();
  });
});
