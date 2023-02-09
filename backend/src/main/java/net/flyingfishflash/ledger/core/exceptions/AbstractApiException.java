package net.flyingfishflash.ledger.core.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import net.flyingfishflash.ledger.core.response.structure.ApiStatus;

public abstract class AbstractApiException extends ErrorResponseException {

  protected AbstractApiException(HttpStatusCode httpStatusCode, String title, String detail) {
    super(httpStatusCode, ProblemDetail.forStatusAndDetail(httpStatusCode, detail), null);
    super.setTitle(title);
  }

  protected AbstractApiException(
      HttpStatusCode httpStatusCode, String title, String detail, Throwable cause) {
    super(httpStatusCode, ProblemDetail.forStatusAndDetail(httpStatusCode, detail), cause);
    super.setTitle(title);
  }

  public ApiStatus getApiStatusCode() {
    return ApiStatus.FAIL;
  }
}
