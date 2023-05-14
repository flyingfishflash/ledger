// angular
import { Injectable } from '@angular/core';

// third party
import { combineLatest, Observable } from 'rxjs';
import { debounceTime, map, shareReplay } from 'rxjs/operators';

// core and shared
import { Logger } from '@core/logging/logger.service';
import {
  EqualObjectsService,
  ObjectEqualityState,
} from '@shared/equal-objects/equal-objects.service';

const log = new Logger('dirty-check.service');

@Injectable({
  providedIn: 'root',
})
export class DirtyCheckService {
  constructor(private equalObjectsService: EqualObjectsService) {}

  dirtyCheck<U>(source: Observable<U>) {
    return <T>(
      valueChanges: Observable<T>,
    ): Observable<ObjectEqualityState> => {
      const isDirty$: Observable<ObjectEqualityState> = combineLatest(
        source,
        valueChanges,
      ).pipe(
        debounceTime(300),
        map(
          ([a, b]) =>
            this.equalObjectsService.isEqual(a, b) /*.equal === false*/,
        ),
        //map(([a, b]) => myIsEqual(a, b) /*.equal === false*/),
        shareReplay({ bufferSize: 1, refCount: true }),
      );
      return isDirty$;
    };
  }
}
