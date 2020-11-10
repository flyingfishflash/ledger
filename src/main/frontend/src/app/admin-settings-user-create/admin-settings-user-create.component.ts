import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable, BehaviorSubject } from 'rxjs';
import { PasswordValidators } from '../shared/password.validator';
import { rolesArray } from '../_models/role'
import { UserService } from '../_services/user.service';

interface CreateUserStatus {
  userDetailsOK: boolean;
  message: string;
}

@Component({
  selector: 'app-admin-settings-user-create',
  templateUrl: './admin-settings-user-create.component.html',
  styleUrls: ['./admin-settings-user-create.component.css'],
})
export class AdminSettingsUserCreateComponent implements OnInit {

  componentHeading = 'Create User';

  userForm: FormGroup;

  rolesArray = rolesArray;

  hide = true;

  createUserStatus$: Observable<CreateUserStatus>;
  createUserStatusSubject = new BehaviorSubject<CreateUserStatus>({ userDetailsOK: null, message: null });

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService
  ) { }

  ngOnInit(): void {

    this.createUserStatus$ = this.createUserStatusSubject.asObservable();

    this.userForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(20)]],
      firstName: ['', [
        Validators.required,
        Validators.maxLength(50)]],
      lastName: ['', [
        Validators.required,
        Validators.maxLength(50)]],
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(50)]],
      role: ['', Validators.required],
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
  }

  onSubmit(): void {

    const createUserStatus: CreateUserStatus = { userDetailsOK: null, message: null };
    this.createUserStatusSubject.next(createUserStatus);

    this.userService.userSignUp(this.userForm.value).subscribe(
      successResponse => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue();
        ps.userDetailsOK = true;
        ps.message = new Date().toLocaleTimeString() + ': ' + successResponse.response.body.message;
        this.createUserStatusSubject.next(ps);
      },
      errorResponse => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue();
        ps.userDetailsOK = false;
        ps.message = new Date().toLocaleTimeString() + ': ' + errorResponse.error.response.body.message;
        this.createUserStatusSubject.next(ps);
      }
    );
  }

  onCancel(): void {
  }
}
