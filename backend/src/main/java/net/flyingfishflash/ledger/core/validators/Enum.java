/*
 https://funofprograming.wordpress.com/2016/09/29/java-enum-validator/
*/

package net.flyingfishflash.ledger.core.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings({"JavaLangClash", "java:S1452"})
public @interface Enum {
  Class<? extends java.lang.Enum<?>> enumClass();

  String message() default "must be a value defined by {enumClass}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean ignoreCase() default false;
}
