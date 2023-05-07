// angular
import { Component, OnDestroy, OnInit } from "@angular/core";
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from "@angular/forms";

// third party
import { Observable } from "rxjs";

// core and shared
import { StorageService } from "@core/storage/storage.service";
import { DirtyCheckService } from "@shared/dirty-check/dirty-check.service";
import { ObjectEqualityState } from "@shared/equal-objects/equal-objects.service";
import { ValidationService } from "@core/validation/validation.service";
import { UtilitiesService } from "@shared/utilities/utilities.service";
import { ProfileService } from "./profile.service";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.css"],
})
export class ProfileComponent implements OnInit, OnDestroy {
  componentHeading = "Profile";
  userDetailsForm: UntypedFormGroup;
  isPasswordHidden = true;
  isDirty$: Observable<ObjectEqualityState>;
  profileUpdateStatus: string;
  userId: number;

  constructor(
    private dirtyCheckService: DirtyCheckService,
    private profileService: ProfileService,
    private storageService: StorageService,
    private utilitiesService: UtilitiesService,
    private formBuilder: UntypedFormBuilder
  ) {}

  ngOnInit() {
    this.userDetailsForm = this.formBuilder.group({
      id: [""],
      email: [
        "",
        [Validators.required, Validators.email, Validators.maxLength(50)],
      ],
      firstName: ["", [Validators.required, Validators.maxLength(50)]],
      lastName: ["", [Validators.required, Validators.maxLength(50)]],
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

    this.profileService.$getSubject.subscribe((response) => {
      this.userDetailsForm.patchValue(response);
    });

    this.profileService.$getProfileUpdateStatus.subscribe((response) => {
      this.profileUpdateStatus = response;
    });

    // Api request get profile
    // User id is determined by backend via authentication process
    if (history.state.data != null) {
      if (history.state.data.userId == null) {
        this.userId = this.storageService.getLoggedInUserId();
        this.profileService.loadDataById(this.userId);
      } else {
        this.userId = history.state.data.userId;
        this.profileService.loadDataById(this.userId);
      }
    } else {
      this.userId = this.storageService.getLoggedInUserId();
      this.profileService.loadDataById(this.userId);
    }

    this.isDirty$ = this.userDetailsForm.valueChanges.pipe(
      this.dirtyCheckService.dirtyCheck(this.profileService.$getSubject)
    );
  }

  ngOnDestroy(): void {
    this.profileService.resetStatus();
  }

  onCancel(): void {
    this.profileService.resetStatus();
    window.history.back();
  }

  onSubmit(): void {
    const userDetailsPayload: any = this.getDirtyValues(this.userDetailsForm);
    if (!this.utilitiesService.isEmptyObject(userDetailsPayload)) {
      this.profileService.userDetailsUpdate(
        userDetailsPayload,
        this.userDetailsForm.controls.id.value
      );
    }
  }

  getDirtyValues(form: any) {
    let dirtyFields = [];
    const dirtyValues = {};

    this.isDirty$.subscribe((val) => {
      dirtyFields = val.differences;
    });
    dirtyFields.forEach((item) => {
      dirtyValues[item] = form.controls[item].value;
    });

    return dirtyValues;
  }
}
