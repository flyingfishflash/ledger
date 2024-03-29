import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable, map, throwError } from 'rxjs'
import { catchError } from 'rxjs/operators'
import { environment } from 'src/environments/environment'
import { ActuatorHealth } from './actuator-health'
import { ActuatorInfo } from './actuator-info'

@Injectable({
  providedIn: 'root',
})
export class ActuatorService {
  constructor(private http: HttpClient) {}

  getHealth(): Observable<ActuatorHealth> {
    return this.http.get<ActuatorHealth>(environment.api.server.url + '/health')
  }

  getHealthStatusSimple(): Observable<boolean> {
    return this.http
      .get<ActuatorHealth>(environment.api.server.url + '/health')
      .pipe(
        map((actuatorHealth) => {
          if (actuatorHealth.status == 'UP') {
            return true
          } else {
            return false
          }
        }),
      )
  }

  getInfo(): Observable<ActuatorInfo> {
    return this.http
      .get<ActuatorInfo>(environment.api.server.url + '/info')
      .pipe(catchError((error) => throwError(() => error)))
  }
}
