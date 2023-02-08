package net.flyingfishflash.ledger.foundation.response.structure.errors;

import java.net.URI;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import net.flyingfishflash.ledger.foundation.IdentifierFactory;
import net.flyingfishflash.ledger.foundation.exceptions.AbstractApiException;
import net.flyingfishflash.ledger.foundation.response.structure.ApiStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationErrorResponse {

  private final String status;
  private ErrorResponseStructure response;

  public record ExceptionCauseDetail(String exception, String localizedMessage) {}

  private static class ErrorResponseStructure {

    private ProblemDetail body;

    ErrorResponseStructure(ProblemDetail body) {
      this.body = body;
    }

    public ProblemDetail getBody() {
      return body;
    }

    public void setBody(ProblemDetail body) {
      this.body = body;
    }
  }

  // -> CustomResponseBodyAdvice.beforeBodyWrite
  public ApplicationErrorResponse(Exception exception) {

    var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    populateProperties(problemDetail, exception);

    this.response = new ErrorResponseStructure(problemDetail);
    this.status = ApiStatus.ERROR.name().toLowerCase();
  }

  // -> AdviceFor[ Non-Api ]Exception
  public ApplicationErrorResponse(
      ApiStatus apiStatus,
      HttpStatus httpStatus,
      Exception exception,
      String title,
      String detail,
      URI instance) {

    var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
    problemDetail.setTitle(title);
    problemDetail.setInstance(instance);
    populateProperties(problemDetail, exception);

    this.status = apiStatus.name().toLowerCase();
    this.response = new ErrorResponseStructure(problemDetail);
  }

  // -> AdviceForApiException
  public ApplicationErrorResponse(
      ApiStatus apiStatus, AbstractApiException apiException, URI instance) {

    var problemDetail = apiException.getBody();

    if (problemDetail.getInstance() == null) {
      problemDetail.setInstance(instance);
    }

    populateProperties(problemDetail, apiException);

    this.status = apiStatus.name().toLowerCase();
    this.response = new ErrorResponseStructure(problemDetail);
  }

  public ErrorResponseStructure getResponse() {
    return response;
  }

  public void setResponse(ErrorResponseStructure response) {
    this.response = response;
  }

  public String getStatus() {
    return status;
  }

  private static void populateProperties(ProblemDetail problemDetail, Exception exception) {
    problemDetail.setProperty(
        "uniqueId", IdentifierFactory.getInstance().identifierWithoutHyphens());
    problemDetail.setProperty("exception", exception.getClass().getSimpleName());
    problemDetail.setProperty("causes", extractCauses(exception));
  }

  private static Throwable exceptionCause(Throwable throwable) {
    Objects.requireNonNull(throwable);
    var rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }

  private static List<ExceptionCauseDetail> extractCauses(Exception exception) {

    ExceptionCauseDetail exceptionCauseDetail;
    List<ExceptionCauseDetail> exceptionCauseDetails = new ArrayList<>();

    if (exception.getCause() != null) {
      Throwable rootCause = exceptionCause(exception);
      exceptionCauseDetail =
          new ExceptionCauseDetail(
              rootCause.getClass().getSimpleName(), rootCause.getLocalizedMessage());
      exceptionCauseDetails.add(exceptionCauseDetail);
    }
    return exceptionCauseDetails;
  }
}
