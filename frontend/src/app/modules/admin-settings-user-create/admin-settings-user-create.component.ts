// angular
import { Component, OnInit } from "@angular/core";
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";

// third party
import { BehaviorSubject, Observable } from "rxjs";

// core and shared
import { rolesArray } from "@shared/users/role";
import { UserService } from "@shared/users/user.service";
import { ValidationService } from "@core/validation/validation.service";

interface CreateUserStatus {
  userDetailsOK: boolean;
  message: string;
}

@Component({
  selector: "app-admin-settings-user-create",
  templateUrl: "./admin-settings-user-create.component.html",
  styleUrls: ["./admin-settings-user-create.component.css"],
})
export class AdminSettingsUserCreateComponent implements OnInit {
  componentHeading = "Create User";
  userForm: UntypedFormGroup;
  rolesArray = rolesArray;
  hide = true;
  isPasswordHidden = true;

  createUserStatus$: Observable<CreateUserStatus>;
  createUserStatusSubject = new BehaviorSubject<CreateUserStatus>({
    userDetailsOK: null,
    message: null,
  });

  constructor(
    private formBuilder: UntypedFormBuilder,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.createUserStatus$ = this.createUserStatusSubject.asObservable();

    this.userForm = this.formBuilder.group({
      username: [
        "",
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(20),
        ],
      ],
      firstName: ["", [Validators.required, Validators.maxLength(50)]],
      lastName: ["", [Validators.required, Validators.maxLength(50)]],
      email: [
        "",
        [Validators.required, Validators.email, Validators.maxLength(50)],
      ],
      role: ["", Validators.required],
      password: [
        "",
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
            /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/,
            { hasSpecialCharacters: true }
          ),
        ],
      ],
    });
  }

  onSubmit(): void {
    const createUserStatus: CreateUserStatus = {
      userDetailsOK: null,
      message: null,
    };
    this.createUserStatusSubject.next(createUserStatus);

    this.userService.userSignUp(this.userForm.value).subscribe(
      (successResponse) => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue();
        ps.userDetailsOK = true;
        ps.message =
          new Date().toLocaleTimeString() +
          ": " +
          successResponse.content.message;
        this.createUserStatusSubject.next(ps);
      },
      (ledgerErrorResponse) => {
        const ps: CreateUserStatus = this.createUserStatusSubject.getValue();
        ps.userDetailsOK = false;
        ps.message =
          new Date().toLocaleTimeString() +
          ": " +
          ledgerErrorResponse.error.content.message;
        this.createUserStatusSubject.next(ps);
      }
    );
  }

  onCancel(): void {}
}
