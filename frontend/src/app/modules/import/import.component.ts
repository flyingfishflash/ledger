// angular
import { Component, OnDestroy, OnInit } from '@angular/core'

// third party
// import { Subscription } from 'rxjs';

// core and shared
// import { environment } from 'environments/environment';
import { StorageService } from '../../core/storage/storage.service'
import { Logger } from '../../core/logging/logger.service'
import { BookService } from '../../shared/books/book.service'
import { concatMap } from 'rxjs/operators'
import { ImportService } from './import.service'

const log = new Logger('import.component')

export interface Comp {
  component: string
  gncCount: number
  sentToAdapter: number
  persisted: number
  ignoredTemplates: number
  ignoredCurrencies: number
}

@Component({
  selector: 'app-import',
  templateUrl: './import.component.html',
  styleUrls: ['./import.component.css'],
})
export class ImportComponent implements OnInit, OnDestroy {
  componentHeading = 'Import'

  uploadFileName = ''
  file: File
  activeBookId: number

  displayedColumnsCommodities: string[] = [
    'Component',
    'Gnc Count',
    'Sent to Adapter',
    'Persisted',
    'Ignored Templates',
    'Ignored Currencies',
  ]
  displayedColumns: string[] = ['Property', 'Value']

  dataSource: Comp[]
  thisdata: Comp[]

  constructor(
    private bookService: BookService,
    private importService: ImportService,
    private storageService: StorageService,
  ) {}

  ngOnInit(): void {
    this.fetchImportStatus()
  }

  fetchImportStatus() {
    this.fetchImportStatusComponentCounts()
  }

  fetchImportStatusComponentCounts() {
    this.importService.getImportStatus().subscribe((res) => {
      res = res || []
      log.debug(res)
      this.dataSource = res.components
    })
  }

  ngOnDestroy() {
    log.debug('ngOnDestroy')
  }

  uploadSelected(files: File[]): void {
    if (files.length > 0) {
      this.file = files[0]
      this.uploadFileName = this.file.name
    }
  }

  onUploadClick(): void {
    if (this.file) {
      const cleanFileName: string = this.file.name.replace(
        /[`~!@#$%^&*()_|+\-=?;:'",.<>{}[\]\\/]/gi,
        '',
      )

      this.bookService
        .postBook(cleanFileName)
        .pipe(
          concatMap((successResponse) => {
            log.debug(successResponse)
            this.activeBookId = successResponse.content.id
            const uploadFileForm = new FormData()
            uploadFileForm.append(
              'bookId',
              '{ "bookId":"' + successResponse.content.id + '" }',
            )
            uploadFileForm.append('file', this.file)
            return this.importService.uploadFile(uploadFileForm)
          }),
        )
        .subscribe({
          next: (successReponse) => {
            log.debug('import success')
            log.debug(successReponse)
            this.storageService.saveActiveBookId(this.activeBookId)
          },
          error: (ledgerErrorResponse) => {
            log.debug('import error')
            log.debug(ledgerErrorResponse)
          },
        })
    }
  }

  onClearClick(): void {
    this.uploadFileName = ''
    // TODO fixs this so file is properly removed
    //this.file = new File();
  }
}
