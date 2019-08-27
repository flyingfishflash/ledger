package net.flyingfishflash.ledger.accounts.exceptions;

// The goal: https://developer.twitter.com/en/docs/basics/response-codes.html
// https://www.codetinkerer.com/2015/12/04/choosing-an-http-status-code.html

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.flyingfishflash.ledger.accounts.AccountController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice(assignableTypes = AccountController.class)
public class AccountExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(AccountExceptionHandler.class);

  /**
   * Provides handling for exceptions throughout this service.
   *
   * @param ex The target exception
   * @param request The current request
   */
  @ExceptionHandler({
    AccountCreateException.class,
    AccountNotFoundException.class,
    NextSiblingAccountNotFoundException.class,
    PrevSiblingAccountNotFoundException.class
  })
  @Nullable
  public final ResponseEntity<ApiError> handleException(
      Exception ex,
      WebRequest request,
      HttpServletResponse response,
      HttpServletRequest servletRequest) {

    HttpHeaders headers = new HttpHeaders();

    logger.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());

    String path = servletRequest.getRequestURI();
    if (servletRequest.getQueryString() != null) {
      path = path + "?" + servletRequest.getQueryString();
    }

    if (ex instanceof AccountCreateException) {
      HttpStatus status = HttpStatus.NOT_FOUND;
      status.value();
      AccountCreateException ace = (AccountCreateException) ex;
      return handleAccountCreateException(ace, headers, status, request, path);

    } else if (ex instanceof AccountNotFoundException) {
      HttpStatus status = HttpStatus.NOT_FOUND;
      AccountNotFoundException anfe = (AccountNotFoundException) ex;
      return handleAccountNotFoundException(anfe, headers, status, request, path);

    } else if (ex instanceof NextSiblingAccountNotFoundException) {
      HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
      NextSiblingAccountNotFoundException nsanfe = (NextSiblingAccountNotFoundException) ex;
      return handleNextSiblingAccountNotFoundException(nsanfe, headers, status, request, path);

    } else if (ex instanceof PrevSiblingAccountNotFoundException) {
      HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
      PrevSiblingAccountNotFoundException psanfe = (PrevSiblingAccountNotFoundException) ex;
      return handlePrevSiblingAccountNotFoundException(psanfe, headers, status, request, path);

    } else {
      if (logger.isWarnEnabled()) {
        logger.warn("Unknown exception type: " + ex.getClass().getName());
      }
      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      return handleExceptionInternal(ex, null, headers, status, request);
    }
  }

  /**
   * Customize the response for AccountCreateException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handleAccountCreateException(
      AccountCreateException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      String path) {
    String detail = ex.getMessage();
    List<String> errors = Collections.singletonList(ex.getMessage());
    return handleExceptionInternal(
        ex,
        new ApiError(path, status.value(), status, ex.getClass().getSimpleName(), errors),
        headers,
        status,
        request);
  }

  /**
   * Customize the response for AccountNotFoundException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handleAccountNotFoundException(
      AccountNotFoundException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      String path) {
    String detail = ex.getMessage();
    List<String> errors = Collections.singletonList(ex.getMessage());
    return handleExceptionInternal(
        ex,
        new ApiError(path, status.value(), status, ex.getClass().getSimpleName(), errors),
        headers,
        status,
        request);
  }

  /**
   * Customize the response for NextSiblingAccountNotFoundException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handleNextSiblingAccountNotFoundException(
      NextSiblingAccountNotFoundException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      String path) {
    String detail = ex.getMessage();
    List<String> errors = Collections.singletonList(ex.getMessage());
    return handleExceptionInternal(
        ex,
        new ApiError(path, status.value(), status, ex.getClass().getSimpleName(), errors),
        headers,
        status,
        request);
  }

  /**
   * Customize the response for PrevSiblingAccountNotFoundException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  protected ResponseEntity<ApiError> handlePrevSiblingAccountNotFoundException(
      PrevSiblingAccountNotFoundException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      String path) {
    String detail = ex.getMessage();
    List<String> errors = Collections.singletonList(ex.getMessage());
    return handleExceptionInternal(
        ex,
        new ApiError(path, status.value(), status, ex.getClass().getSimpleName(), errors),
        headers,
        status,
        request);
  }

  /**
   * Customize the response for ContentNotAllowedException.
   *
   * @param ex The exception
   * @param headers The headers to be written to the response
   * @param status The selected response status
   * @return a {@code ResponseEntity} instance
   */
  /*
    protected ResponseEntity<ApiError> handleContentNotAllowedException(
        ContentNotAllowedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
      List<String> errorMessages =
          ex.getErrors().stream()
              .map(
                  contentError ->
                      contentError.getObjectName() + " " + contentError.getDefaultMessage())
              .collect(Collectors.toList());

      return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
    }
  */
  /**
   * A single place to customize the response body of all Exception types.
   *
   * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE} request
   * attribute and creates a {@link ResponseEntity} from the given body, headers, and status.
   *
   * @param ex The exception
   * @param body The body for the response
   * @param headers The headers for the response
   * @param status The response status
   * @param request The current request
   */
  protected ResponseEntity<ApiError> handleExceptionInternal(
      Exception ex,
      @Nullable ApiError body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }
    return new ResponseEntity<>(body, headers, status);
  }
}
