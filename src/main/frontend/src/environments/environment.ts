// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// Note the server here (localhost) shoudl be the same client name as the host
// otherwise there will be issues saving the session cookie

export const environment = {
  production: false,
  api: {
    url: 'http://localhost:8181/api/v1/ledger',
    version: 'v1'
  },
  wsEndpoint: 'ws://localhost:8181/ws',
  authApi: {
    url: 'http://localhost:8181/api/v1/ledger'
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
