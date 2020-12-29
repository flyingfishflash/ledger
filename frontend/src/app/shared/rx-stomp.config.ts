import { InjectableRxStompConfig } from "@stomp/ng2-stompjs";
import { environment } from "environments/environment";
// import { TokenStorageService } from '../_services/token-storage.service';

// https://stomp-js.github.io/guide/ng2-stompjs/ng2-stomp-with-angular7.html

export const rxStompConfig: InjectableRxStompConfig = {
  brokerURL: environment.wsEndpoint,

  // Headers
  // Typical keys: login, passcode, host
  connectHeaders: {
    login: "admi",
    // passcode: 'admin',
    authorization: "",
  },

  // Interval in milliseconds
  // Set to 0 to disable
  heartbeatIncoming: 0, // Typical value 0
  heartbeatOutgoing: 20000, // Typical value 20000

  // Milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 500 (500 milli seconds)
  reconnectDelay: 5000,

  // Will log diagnostics on console
  // It can be quite verbose, not recommended in production
  // Skip this key to stop logging to console
  debug: (msg: string): void => {
    console.log(new Date(), msg);
  },
};
