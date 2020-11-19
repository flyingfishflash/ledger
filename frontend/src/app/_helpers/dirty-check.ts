import { Observable, combineLatest } from 'rxjs';
import { ObjectEqualityState, myIsEqual } from './my-is-equal';
import { debounceTime, map, shareReplay } from 'rxjs/operators';

export function dirtyCheck<U>(source: Observable<U>) {
  return <T>(valueChanges: Observable<T>): Observable<ObjectEqualityState> => {
    const isDirty$: Observable<ObjectEqualityState> = combineLatest(
      source,
      valueChanges,
    ).pipe(
      debounceTime(300),
      map(([a, b]) => myIsEqual(a, b) /*.equal === false*/),
      shareReplay({ bufferSize: 1, refCount: true }),
    );
    return isDirty$;
  };
}
