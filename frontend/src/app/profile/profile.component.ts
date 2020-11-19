import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { ProfileService } from './profile.service';
import { PasswordValidators } from '../shared/password.validator';
import { ObjectEqualityState } from '../_helpers/my-is-equal';
import { isEmptyObject } from '../_helpers/is-empty-object';
import { dirtyCheck } from '../_helpers/dirty-check';
import { BasicAuthService } from '../_services/basic-auth.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  componentHeading = 'Profile';

  userDetailsForm: FormGroup;

  hide = true;

  isDirty$: Observable<ObjectEqualityState>;

  profileUpdateStatus: string;

  userId: number;

  constructor(
    private authenticationService: BasicAuthService,
    private profileService: ProfileService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {

    this.userDetailsForm = this.formBuilder.group({
      id: [''],
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(50)]],
      firstName: ['', [
        Validators.required,
        Validators.maxLength(50)]],
      lastName: ['', [
        Validators.required,
        Validators.maxLength(50)]],
      password: ['', [
        Validators.minLength(8),
        Validators.maxLength(128),
        // check whether the entered password has whitespace
        PasswordValidators.patternValidator(/^\S+$/, { noWhitespace: true }),
        // check whether the entered password has a lower-case letter
        PasswordValidators.patternValidator(/[a-z]/, { hasLowercase: true }),
        // check whether the entered password has upper case letter
        PasswordValidators.patternValidator(/[A-Z]/, { hasUppercase: true }),
        // check whether the entered password has a number
        PasswordValidators.patternValidator(/\d/, { hasNumber: true }),
        // check whether the entered password has a special character
        PasswordValidators.patternValidator(/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/, { hasSpecialCharacters: true }),]]
    });

    this.profileService.$getSubject.subscribe(response => {
      this.userDetailsForm.patchValue(response);
    });

    this.profileService.$getProfileUpdateStatus.subscribe(response => {
      this.profileUpdateStatus = response;
    });

    // Api request get profile
    // User id is determined by backend via authentication process
    if (history.state.data != null) {
      if (history.state.data.userId == null) {
        this.userId = this.authenticationService.getLoggedInUserId();
        this.profileService.loadDataById(this.userId);
      } else {
        this.userId = history.state.data.userId;
        this.profileService.loadDataById(this.userId);
      }
    } else {
      this.userId = this.authenticationService.getLoggedInUserId();
      this.profileService.loadDataById(this.userId);
    }

    this.isDirty$ = this.userDetailsForm.valueChanges.pipe(dirtyCheck(this.profileService.$getSubject));
  }

  ngOnDestroy(): void {
    this.profileService.resetStatus();
  }

  onCancel(): void { 
    this.profileService.resetStatus();
    window.history.back();
  }

  onSubmit(): void {

    const userDetailsPayload:any = this.getDirtyValues(this.userDetailsForm);
    if (!isEmptyObject(userDetailsPayload)) {
      this.profileService.userDetailsUpdate(userDetailsPayload, this.userDetailsForm.controls.id.value);
    }
  }

  getDirtyValues(form: any) {

    let dirtyFields = [];
    const dirtyValues = {};

    this.isDirty$.subscribe(val => { dirtyFields = val.differences; });
    dirtyFields.forEach(item => {
      dirtyValues[item] = form.controls[item].value;
    });

    return dirtyValues;
  }
}
