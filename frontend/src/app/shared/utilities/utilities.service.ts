// angular
import { Injectable } from '@angular/core';

// core and shared
import { Logger } from '@core/logging/logger.service';

const log = new Logger('utilities.service');

@Injectable({
  providedIn: 'root',
})
export class UtilitiesService {
  isEmptyObject(obj) {
    return JSON.stringify(obj) === '{}';
  }
}
