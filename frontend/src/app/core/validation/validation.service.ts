// angular
import { Injectable } from "@angular/core";
import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

@Injectable({
  providedIn: "root",
})
export class ValidationService {
  public static getValidationErrorMessage(
    validatorName: string,
    validatorValue?: any,
    labelName?: string
  ): any {
    const config = {
      required: "Field is required.",
      invalidPassword:
        "Invalid password. Password must be at least 6 characters long, and contain a number.",
      maxlength: `The field can't contain more than ${validatorValue.requiredLength} characters.`,
      minlength: `The field must contain atleast ${validatorValue.requiredLength} characters.`,
    };

    return config[validatorName];
  }

  public static patternValidator(
    regex: RegExp,
    error: ValidationErrors
  ): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (!control.value) {
        // if control is empty return no error
        return null;
      }

      // test the value of the control against the regexp supplied
      const valid = regex.test(control.value);

      // if true, return no error (no error), else return error passed in the second parameter
      return valid ? null : error;
    };
  }

  /* 
  public static passwordValidator(control: AbstractControl): any {
    if (!control.value) {
      return;
    }

    // {6,100} - Assert password is between 6 and 100 characters
    // (?=.*[0-9]) - Assert a string has at least one number
    // (?!.*\s) - Spaces are not allowed
    return control.value.match(
      /^(?=.*\d)(?=.*[a-zA-Z!@#$%^&*])(?!.*\s).{6,100}$/
    )
      ? ""
      : { invalidPassword: true };
  }
 */
}
