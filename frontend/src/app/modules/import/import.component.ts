// angular
import { Component, OnDestroy, OnInit } from '@angular/core'

// third party
// import { Subscription } from 'rxjs';

// core and shared
// import { environment } from 'environments/environment';
import { FormsModule } from '@angular/forms'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatTooltipModule } from '@angular/material/tooltip'
import { concatMap } from 'rxjs/operators'
import { Logger } from '../../core/logging/logger.service'
import { StorageService } from '../../core/storage/storage.service'
import { BookService } from '../../shared/books/book.service'
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
  standalone: true,
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatTooltipModule,
  ],
})
export class ImportComponent implements OnInit, OnDestroy {
  componentHeading = 'Import'

  uploadFileName = ''
  file: File | undefined
  activeBookId: number = 0

  displayedColumnsCommodities: string[] = [
    'Component',
    'Gnc Count',
    'Sent to Adapter',
    'Persisted',
    'Ignored Templates',
    'Ignored Currencies',
  ]
  displayedColumns: string[] = ['Property', 'Value']

  // dataSource: Comp[]
  // thisdata: Comp[]

  constructor(
    private bookService: BookService,
    private importService: ImportService,
    private storageService: StorageService,
  ) {}

  ngOnInit(): void {
    // this.fetchImportStatus()
  }

  // fetchImportStatus() {
  //   this.fetchImportStatusComponentCounts()
  // }

  // fetchImportStatusComponentCounts() {
  //   this.importService.getImportStatus().subscribe((res) => {
  //     res = res || []
  //     log.debug(res)
  //     this.dataSource = res.components
  //   })
  // }

  ngOnDestroy() {
    log.debug('ngOnDestroy')
  }

  uploadSelected(files: File[]): void {
    if (files.length > 0) {
      if (files[0] !== undefined) {
        this.file = files[0]
        this.uploadFileName = this.file.name
      } else {
        throw new Error('Error uploading selected file')
      }
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

            if (this.file !== undefined) {
              uploadFileForm.append('file', this.file)
            } else {
              log.debug('this.file is undefined')
            }
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
