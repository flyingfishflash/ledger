package net.flyingfishflash.ledger.core;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

/**
 * <i>Re-Implementation of Spring Boot's {@link
 * org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 * BasicErrorController}.</i>
 *
 * <p>Basic global error controller, rendering {@link ErrorAttributes}. More specific errors can be
 * handled either using Spring MVC abstractions (e.g. {@code @ExceptionHandler}) or by adding
 * servlet server error pages.
 *
 * @see ErrorAttributes
 * @see ErrorProperties
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends AbstractErrorController {

  private final ErrorProperties errorProperties;

  public CustomErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
    super(errorAttributes);
    this.errorProperties = errorProperties;
  }

  @GetMapping
  public ResponseEntity<Response<ProblemDetail>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
      return new ResponseEntity<>(status);
    }

    var defaultErrorAttributes = new DefaultErrorAttributes();
    var throwable = defaultErrorAttributes.getError(new ServletWebRequest(request));
    var errorAttributes = getErrorAttributes(request, getErrorAttributeOptions(request));
    var problemDetail = ProblemDetailUtility.createProblemDetail(throwable, errorAttributes);
    var response = new Response<>(problemDetail, request.getMethod(), "Message about the problem");
    return new ResponseEntity<>(response, status);
  }

  public ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request) {
    ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
    if (this.errorProperties.isIncludeException()) {
      options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
    }
    if (isIncludeStackTrace(request)) {
      options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
    }
    if (isIncludeMessage(request)) {
      options = options.including(ErrorAttributeOptions.Include.MESSAGE);
    }
    if (isIncludeBindingErrors(request)) {
      options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
    }
    return options;
  }

  /**
   * Determine if the stacktrace attribute should be included.
   *
   * @param request the source request
   * @return if the stacktrace attribute should be included
   */
  protected boolean isIncludeStackTrace(HttpServletRequest request) {
    return switch (getErrorProperties().getIncludeStacktrace()) {
      case ALWAYS -> true;
      case ON_PARAM -> getTraceParameter(request);
      default -> false;
    };
  }

  /**
   * Determine if the message attribute should be included.
   *
   * @param request the source request
   * @return if the message attribute should be included
   */
  protected boolean isIncludeMessage(HttpServletRequest request) {
    return switch (getErrorProperties().getIncludeMessage()) {
      case ALWAYS -> true;
      case ON_PARAM -> getMessageParameter(request);
      default -> false;
    };
  }

  /**
   * Determine if the errors attribute should be included.
   *
   * @param request the source request
   * @return if the errors attribute should be included
   */
  protected boolean isIncludeBindingErrors(HttpServletRequest request) {
    return switch (getErrorProperties().getIncludeBindingErrors()) {
      case ALWAYS -> true;
      case ON_PARAM -> getErrorsParameter(request);
      default -> false;
    };
  }

  protected ErrorProperties getErrorProperties() {
    return this.errorProperties;
  }
}
