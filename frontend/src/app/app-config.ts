// angular
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

interface Config {
  apiServer: {
    url: string;
    urlAuth: string;
  };
}

export interface IAppConfig {
  apiServer: {
    url: string;
    urlAuth: string;
  };
  load: () => Promise<void>;
}

@Injectable()
export class AppConfig implements IAppConfig {
  public apiServer: {
    url: string;
    urlAuth: string;
  };

  constructor(private readonly http: HttpClient) {}

  public load(): Promise<void> {
    return this.http
      .get<Config>("assets/config.json")
      .toPromise()
      .then((config) => {
        this.apiServer = config.apiServer;
      });
  }
}
export function initConfig(config: AppConfig): () => Promise<void> {
  return () => config.load();
}
