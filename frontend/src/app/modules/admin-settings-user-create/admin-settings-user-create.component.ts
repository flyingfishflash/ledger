// angular
import { Component, OnInit } from '@angular/core'
import {
  FormsModule,
  ReactiveFormsModule,
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms'

// third party
import { BehaviorSubject, Observable } from 'rxjs'

// core and shared
import { AsyncPipe, NgClass, NgFor, NgIf } from '@angular/common'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatRadioModule } from '@angular/material/radio'
import { MatTooltipModule } from '@angular/material/tooltip'
import { RouterLink } from '@angular/router'
import { Logger } from '../../core/logging/logger.service'
import { ValidationService } from '../../core/validation/validation.service'
import { rolesArray } from '../../shared/users/role'
import { UserService } from '../../shared/users/user.service'

const LOGGER = new Logger('login.component')

interface CreateUserStatus {
  userDetailsOk: boolean
  message: string
}

@Component({
  selector: 'app-admin-settings-user-create',
  templateUrl: './admin-settings-user-create.component.html',
  styleUrls: ['./admin-settings-user-create.component.css'],
  standalone: true,
  imports: [
    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    NgIf,
    MatRadioModule,
    NgFor,
    MatButtonModule,
    MatIconModule,
    NgClass,
    MatTooltipModule,
    RouterLink,
    AsyncPipe,
  ],
})
export class AdminSettingsUserCreateComponent implements OnInit {
  componentHeading = 'Create User'
  userForm: UntypedFormGroup
  rolesArray = rolesArray
  hide = true
  isPasswordHidden = true

  createUserStatus$ = new Observable<CreateUserStatus>()
  createUserStatusSubject = new BehaviorSubject<CreateUserStatus>({
    userDetailsOk: false,
    message: '',
  })

  constructor(
    private formBuilder: UntypedFormBuilder,
    private userService: UserService,
  ) {
    this.userForm = this.formBuilder.group({
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(20),
        ],
      ],
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(50)],
      ],
      role: ['', Validators.required],
      password: [
        '',
        [
          Validators.minLength(8),
          Validators.maxLength(128),
          // check whether the entered password has whitespace
          ValidationService.patternValidator(/^\S+$/, { noWhitespace: true }),
          // check whether the entered password has a lower-case letter
          ValidationService.patternValidator(/[a-z]/, { hasLowercase: true }),
          // check whether the entered password has upper case letter
          ValidationService.patternValidator(/[A-Z]/, { hasUppercase: true }),
          // check whether the entered password has a number
          ValidationService.patternValidator(/\d/, { hasNumber: true }),
          // check whether the entered password has a special character
          ValidationService.patternValidator(
            /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/,
            { hasSpecialCharacters: true },
          ),
        ],
      ],
    })
  }

  ngOnInit(): void {
    this.createUserStatus$ = this.createUserStatusSubject.asObservable()
  }

  onSubmit(): void {
    const createUserStatus: CreateUserStatus = {
      userDetailsOk: false,
      message: '',
    }
    this.createUserStatusSubject.next(createUserStatus)

    this.userService.userSignUp(this.userForm.value).subscribe({
      next: (successResponse) => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue()
        ps.userDetailsOk = true
        ps.message =
          new Date().toLocaleTimeString() +
          ': ' +
          successResponse.content.message
        this.createUserStatusSubject.next(ps)
      },
      error: (ledgerErrorResponse) => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue()
        ps.userDetailsOk = false
        ps.message =
          new Date().toLocaleTimeString() +
          ': ' +
          ledgerErrorResponse.error.content.message
        this.createUserStatusSubject.next(ps)
      },
    })
  }

  onCancel(): void {
    LOGGER.debug('cancel clicked')
  }
}
