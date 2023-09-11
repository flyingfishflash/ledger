import { Injectable } from '@angular/core'
import { Logger } from '../../core/logging/logger.service'

const log = new Logger('utilities.service')

@Injectable({
  providedIn: 'root',
})
export class UtilitiesService {
  isEmptyObject(obj: any): boolean {
    return JSON.stringify(obj) === '{}'
  }
}
