// angular
import { Injectable } from "@angular/core";

// third party
import { Observable } from "rxjs";
import { combineLatest } from "rxjs";
import { debounceTime } from "rxjs/operators";
import { map } from "rxjs/operators";
import { shareReplay } from "rxjs/operators";

// core and shared
import { Logger } from "@core/logging/logger.service";
import { EqualObjectsService } from "@shared/equal-objects/equal-objects.service";
import { ObjectEqualityState } from "@shared/equal-objects/equal-objects.service";

const log = new Logger("dirty-check.service");

@Injectable({
  providedIn: "root",
})
export class DirtyCheckService {
  constructor(private equalObjectsService: EqualObjectsService) {}

  dirtyCheck<U>(source: Observable<U>) {
    return <T>(
      valueChanges: Observable<T>
    ): Observable<ObjectEqualityState> => {
      const isDirty$: Observable<ObjectEqualityState> = combineLatest(
        source,
        valueChanges
      ).pipe(
        debounceTime(300),
        map(
          ([a, b]) =>
            this.equalObjectsService.isEqual(a, b) /*.equal === false*/
        ),
        //map(([a, b]) => myIsEqual(a, b) /*.equal === false*/),
        shareReplay({ bufferSize: 1, refCount: true })
      );
      return isDirty$;
    };
  }
}
