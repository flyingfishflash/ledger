// angular
import { Component, OnDestroy, OnInit } from "@angular/core";

// third party
import { Subscription } from "rxjs";
// eslint-disable-next-line import/no-extraneous-dependencies
import { Message } from "@stomp/stompjs";
import { InjectableRxStompConfig, RxStompService } from "@stomp/ng2-stompjs";

// core and shared
import { environment } from "environments/environment";
import { ImportService } from "./import.service";
import { StorageService } from "@core/storage/storage.service";
import { Logger } from "@core/logging/logger.service";
import { BookService } from "@shared/books/book.service";
import { rxStompConfig } from "@shared/rx-stomp.config";
//import { stringify } from "@angular/compiler/src/util";
import { concatMap } from "rxjs/operators";

const log = new Logger("import.component");

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
  activeBookId: Number;

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
    private bookService: BookService,
    private importService: ImportService,
    private storageService: StorageService,
    private rxStompService: RxStompService
  ) {}

  ngOnInit(): void {
    this.fetchImportStatus();

    this.initStomp();

    this.importMessagesSubscription = this.rxStompService
      .watch("/user/import/status/messages")
      .subscribe((message: Message) => {
        log.debug("received a message!");
        this.receivedMessages.push(message.body);
        log.debug(message.body);
      });

    this.importCountsSubscription = this.rxStompService
      .watch("/user/import/status/counts")
      .subscribe((message: Message) => {
        // log.debug('received a count!')
        // let messageJson = JSON.parse(message.body);
        // log.debug('should be fetching new counts');
        // this.fetchImportStatusComponentCounts();
        // log.debug(messageJson);
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
      log.debug(res);
      this.dataSource = res.components;
    });
  }

  ngOnDestroy() {
    this.importMessagesSubscription.unsubscribe();
    this.importCountsSubscription.unsubscribe();
    this.rxStompService.deactivate();
    log.debug("ngOnDestroy");
  }

  uploadSelected(files: File[]): void {
    if (files.length > 0) {
      this.file = files[0];
      this.uploadFileName = this.file.name;
    }
  }

  onUploadClick(): void {
    if (this.file) {
      const cleanFileName: string = this.file.name.replace(
        /[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi,
        ""
      );

      this.bookService
        .postBook(cleanFileName)
        .pipe(
          concatMap((successResponse) => {
            log.debug(successResponse);
            this.activeBookId = successResponse.response.body.id;
            var uploadFileForm = new FormData();
            uploadFileForm.append(
              "bookId",
              '{ "bookId":"' + successResponse.response.body.id + '" }'
            );
            uploadFileForm.append("file", this.file);
            return this.importService.uploadFile(uploadFileForm);
          })
        )
        .subscribe(
          (successReponse) => {
            log.debug(successReponse);
            log.debug("import success");
            this.storageService.saveActiveBookId(this.activeBookId);
          },
          (ledgerErrorResponse) => {
            log.debug("import error");
          }
        );
    }
  }

  onClearClick(): void {
    this.uploadFileName = "";
    this.file = null;
  }

  private initStomp() {
    log.debug("initStomp()");
    const stompConfig: InjectableRxStompConfig = {
      ...rxStompConfig,
      brokerURL: environment.wsEndpoint,
      connectHeaders: {
        login: this.storageService.getLoggedInUserName(),
        authorization: null,
      },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 5000,
      debug: (msg: string): void => {
        log.debug(new Date(), msg);
      },
      beforeConnect: () => {
        log.debug("%c called before connect", "color: blue");
      },
    };

    this.rxStompService.configure(stompConfig);
    this.rxStompService.activate();
  }
}
