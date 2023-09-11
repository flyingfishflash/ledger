import { NgClass, NgIf } from '@angular/common'
import { Component, OnInit } from '@angular/core'
import {
  FormGroup,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatIconModule } from '@angular/material/icon'
import { MatInputModule } from '@angular/material/input'
import { MatTooltipModule } from '@angular/material/tooltip'
import { RouterLink } from '@angular/router'
import { Logger } from 'src/app/core/logging/logger.service'
import { StorageService } from 'src/app/core/storage/storage.service'
import { ValidationService } from 'src/app/core/validation/validation.service'
import { UtilitiesService } from 'src/app/lib/utilities/utilities.service'
import { ProfileService } from './profile.service'

const log = new Logger('profile componenet')

class UserDetailsFormValues {
  private id: number = 0
  private email: string = ''
  private firstName: string = ''
  private lastName: string = ''
  private password: string = ''

  constructor(propertyValues: any) {
    Object.assign(this, propertyValues)
  }

  differences(other: UserDetailsFormValues): any {
    const updatedValues: any = {}

    Object.keys(this).forEach((key) => {
      if (
        this[key as keyof UserDetailsFormValues] !==
        other[key as keyof UserDetailsFormValues]
      ) {
        updatedValues[key] = other[key as keyof UserDetailsFormValues]
      }
    })

    return updatedValues
  }
}

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  standalone: true,
  imports: [
    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    NgIf,
    MatButtonModule,
    MatIconModule,
    NgClass,
    MatTooltipModule,
    RouterLink,
  ],
})
export class ProfileComponent implements OnInit {
  componentHeading: string = 'Profile'
  userDetailsForm: FormGroup
  userDetailsFormInitial: UserDetailsFormValues | undefined
  isPasswordHidden: boolean = true
  profileUpdateStatus: string = ''
  userId: number = 0

  constructor(
    private profileService: ProfileService,
    private storageService: StorageService,
    private utilitiesService: UtilitiesService,
    private formBuilder: NonNullableFormBuilder,
  ) {
    this.userDetailsForm = this.formBuilder.group({
      id: [''],
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(50)],
      ],
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
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

  ngOnInit() {
    // User id is determined by backend via authentication process
    if (history.state.data != null) {
      if (history.state.data.userId == 0) {
        this.userId = this.storageService.getLoggedInUserId()
      } else {
        this.userId = history.state.data.userId
      }
    } else {
      this.userId = this.storageService.getLoggedInUserId()
    }

    this.profileService.getProfileById(this.userId).subscribe((response) => {
      this.userDetailsForm?.patchValue(response)
      this.userDetailsFormInitial = new UserDetailsFormValues(
        this.userDetailsForm?.value,
      )
    })
  }

  onCancel(): void {
    log.debug(
      'we should check for unsaved entries and navigate back to previous route',
    )
  }

  onSubmit(): void {
    const userDetailsFormCurrent = new UserDetailsFormValues(
      this.userDetailsForm?.value,
    )

    const userDetailsPayload = this.userDetailsFormInitial?.differences(
      userDetailsFormCurrent,
    )
    log.debug(userDetailsPayload)

    if (!this.utilitiesService.isEmptyObject(userDetailsPayload)) {
      log.debug('current form differs from intial form')
      log.debug('initial: ' + JSON.stringify(this.userDetailsFormInitial))
      log.debug('current: ' + JSON.stringify(this.userDetailsForm?.value))

      this.profileService.userDetailsUpdate(
        userDetailsPayload,
        this.userDetailsForm?.controls['id']?.value,
      )

      this.userDetailsFormInitial = new UserDetailsFormValues(
        this.userDetailsForm?.value,
      )
    }
  }
}
