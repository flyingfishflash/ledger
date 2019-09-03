package net.flyingfishflash.ledger.utilities.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

  private static final Logger logger = LoggerFactory.getLogger(CustomErrorAttributes.class);

  private MessageSource messageSource;

  public CustomErrorAttributes(MessageSource messageSource) {
    super(true);
    this.messageSource = messageSource;
  }

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

    Throwable throwable = this.getError(webRequest);
    if (throwable != null) {
      Throwable cause = throwable.getCause();
      if (cause != null) {
        Map causeErrorAttributes = new HashMap<>();
        causeErrorAttributes.put("exception", cause.getClass().getName());
        causeErrorAttributes.put("message", cause.getMessage());
        errorAttributes.put("cause", causeErrorAttributes);
      }
    } else {
      logger.info("WebRequest.getError() is null." + webRequest.getContextPath());
    }

    resolveBindingErrors(errorAttributes);

    return errorAttributes;
  }

  private void resolveBindingErrors(Map<String, Object> errorAttributes) {
    List<ObjectError> errors = (List<ObjectError>) errorAttributes.get("errors");
    if (errors == null) {
      return;
    }

    List<String> errorMessages = new ArrayList<>();
    //List<Object> validationErrorMessages = new ArrayList<>();
    // FieldErrors fe;
    for (ObjectError error : errors) {
      String resolved = messageSource.getMessage(error, Locale.US);
      if (error instanceof FieldError) {
        FieldError fieldError = (FieldError) error;
        // fe = new FieldErrors(fieldError.getField(), fieldError.getRejectedValue(), resolved);
        errorMessages.add(
            fieldError.getField()
                + ": "
                + resolved
                + " (rejected value: "
                + fieldError.getRejectedValue()
                + ")");
        // validationErrorMessages.add(fe);
      } else {
        errorMessages.add(resolved);
      }
    }
    // using a key other than errors, or removing this line will restore the default output for
    // field validation errors
    errorAttributes.put("errors", errorMessages);
    // errorAttributes.put("validation_field_errors", validationErrorMessages);
  }
}
