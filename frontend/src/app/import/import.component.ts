import { Component, OnDestroy, OnInit, AfterContentInit } from "@angular/core";
// import { HttpClient } from '@angular/common/http';

import { RxStompService, InjectableRxStompConfig } from "@stomp/ng2-stompjs";
import { Message } from "@stomp/stompjs";
import { Subscription } from "rxjs";

import { environment } from "src/environments/environment";
import { ImportService } from "./import-service.service";
// import { TokenStorageService } from '../_services/token-storage.service';

import { rxStompConfig } from "src/app/shared/rx-stomp.config";
import { BasicAuthService } from "../_services/basic-auth.service";

export interface Comp {
  component: string;
  gncCount: number;
  sentToAdapter: number;
  persisted: number;
  ignoredTemplates: number;
  ignoredCurrencies: number;
}

@Component({
  selector: "app-import",
  templateUrl: "./import.component.html",
  styleUrls: ["./import.component.css"],
})
export class ImportComponent implements OnInit, OnDestroy {
  componentHeading = "Import";

  uploadFileName = "";
  file: File;

  importMessagesSubscription: Subscription;
  importCountsSubscription: Subscription;

  receivedMessages: string[] = [];

  displayedColumnsCommodities: string[] = [
    "Component",
    "Gnc Count",
    "Sent to Adapter",
    "Persisted",
    "Ignored Templates",
    "Ignored Currencies",
  ];
  displayedColumns: string[] = ["Property", "Value"];

  dataSource: Comp[];
  thisdata: Comp[];

  constructor(
    private importService: ImportService,
    private basicAuthService: BasicAuthService,
    // private tokenStorageService: TokenStorageService,
    private rxStompService: RxStompService
  ) {
    console.log("constructing import component");
  }

  ngOnInit(): void {
    console.log("ngOnInit()");

    this.fetchImportStatus();

    this.initStomp();

    this.importMessagesSubscription = this.rxStompService
      .watch("/user/import/status/messages")
      .subscribe((message: Message) => {
        console.log("received a message!");
        this.receivedMessages.push(message.body);
        console.log(message.body);
      });

    this.importCountsSubscription = this.rxStompService
      .watch("/user/import/status/counts")
      .subscribe((message: Message) => {
        // console.log('received a count!')
        // let messageJson = JSON.parse(message.body);
        // console.log('should be fetching new counts');
        // this.fetchImportStatusComponentCounts();
        // console.log(messageJson);
        // this.dataSource = (messageJson["components"]);
      });

    // this.rxStompService.publish({destination: '/app/sendImportStatusCounts', body: 'request' });
  }

  fetchImportStatus() {
    this.fetchImportStatusComponentCounts();
  }

  fetchImportStatusComponentCounts() {
    this.importService.getImportStatus().subscribe((res) => {
      res = res || [];
      console.log(res);
      this.dataSource = res.components;
    });
  }

  ngOnDestroy() {
    this.importMessagesSubscription.unsubscribe();
    this.importCountsSubscription.unsubscribe();
    this.rxStompService.deactivate();
    console.log("ngOnDestroy");
  }

  uploadSelected(files: File[]): void {
    if (files.length > 0) {
      this.file = files[0];
      this.uploadFileName = this.file.name;
    }
  }

  onUploadClick(): void {
    if (this.file) {
      const formData: FormData = new FormData();
      formData.append("file", this.file);

      this.importService.uploadFile(formData).subscribe(
        (res) => {
          console.log("post completed, should be changing datasource");
          this.dataSource = res.components;
          console.log(res);
        },
        (err) => {
          console.log(err);
        }
      );
    }
  }

  onClearClick(): void {
    this.uploadFileName = "";
    this.file = null;
  }

  private initStomp() {
    console.log("initStomp()");
    const stompConfig: InjectableRxStompConfig = Object.assign(
      {},
      rxStompConfig,
      {
        brokerURL: environment.wsEndpoint,
        connectHeaders: {
          login: this.basicAuthService.getLoggedInUserName(),
          authorization: null,
        },
        heartbeatIncoming: 0,
        heartbeatOutgoing: 20000,
        reconnectDelay: 5000,
        debug: (msg: string): void => {
          console.log(new Date(), msg);
        },
        beforeConnect: () => {
          console.log("%c called before connect", "color: blue");
        },
      }
    );

    this.rxStompService.configure(stompConfig);
    this.rxStompService.activate();
  }
}
