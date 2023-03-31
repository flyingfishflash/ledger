/*
 https://funofprograming.wordpress.com/2016/09/29/java-enum-validator/
*/

package net.flyingfishflash.ledger.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<Enum, String> {
  private Enum annotation;

  @Override
  public void initialize(Enum annotation) {
    this.annotation = annotation;
  }

  @Override
  public boolean isValid(
      String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {
    var result = false;

    Object[] enumValues = this.annotation.enumClass().getEnumConstants();

    if (enumValues != null && valueForValidation != null) {
      for (Object enumValue : enumValues) {
        if (valueForValidation.equals(enumValue.toString())
            || (this.annotation.ignoreCase()
                && valueForValidation.equalsIgnoreCase(enumValue.toString()))) {
          result = true;
          break;
        }
      }
    }

    return result;
  }
}
