package net.flyingfishflash.ledger.unit.core.response.advice;

import static org.assertj.core.api.Assertions.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import net.flyingfishflash.ledger.core.exceptions.ApiException;
import net.flyingfishflash.ledger.core.response.advice.ApiExceptionAdvice;
import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

/** Unit tests for {@link ApiExceptionAdvice} */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("ApiExceptionAdvice")
class ApiExceptionAdviceTests {

  private static final ApiExceptionAdvice apiExceptionAdvice = new ApiExceptionAdvice();

  private static final MockHttpServletRequest request =
      new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");

  private static final ApiException apiExceptionWithCause =
      new ApiException(
          HttpStatus.NO_CONTENT,
          "Lorem Ipsum With Cause",
          new IllegalArgumentException("Illegal Argument Exception"));

  private static List<ApiException> apiExceptions() {

    return Arrays.asList(
        new ApiException(HttpStatus.NO_CONTENT, "Lorem Ipsum"), apiExceptionWithCause);
  }

  @Order(1)
  @ParameterizedTest
  @MethodSource("apiExceptions")
  void handleExceptionReturnsEssentiallyValidResponse(ApiException apiException) {
    ResponseEntity<?> response = apiExceptionAdvice.handleException(request, apiException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isInstanceOf(Response.class);
  }

  // Verify response entity fields are as expected after being reconstructed into a
  // ResponseEntity<Response<ProblemDetail>>

  @Test
  // It's known apiException.getBody().getInstance().getPath() will not return null
  @SuppressWarnings("ConstantConditions")
  void responseBodyInstancePathIsEqualToApiExceptionBodyInstancePath() {
    ApiException apiExceptionWithInstance = new ApiException(HttpStatus.NO_CONTENT, "Lorem Ipsum");
    apiExceptionWithInstance.setInstance(URI.create("/nonNull/instance"));

    ResponseEntity<Response<ProblemDetail>> response =
        apiExceptionAdvice.handleException(request, apiExceptionWithInstance);

    Response<ProblemDetail> responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    var uriPath = responseBody.getInstance();
    assertThat(uriPath).isEqualTo(apiExceptionWithInstance.getBody().getInstance().getPath());
  }

  @ParameterizedTest
  @MethodSource("apiExceptions")
  void responseBodyInstancePathIsEqualToServletRequestUri(ApiException apiException) {
    ResponseEntity<Response<ProblemDetail>> response =
        apiExceptionAdvice.handleException(request, apiException);

    Response<ProblemDetail> responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    var uriPath = responseBody.getInstance();
    var requestUri = request.getRequestURI();
    assertThat(uriPath).isEqualTo(requestUri);
  }

  @ParameterizedTest
  @MethodSource("apiExceptions")
  void responseBodyTitleIsEqualToExceptionBodyTitle(ApiException apiException) {
    ResponseEntity<Response<ProblemDetail>> response =
        apiExceptionAdvice.handleException(request, apiException);

    Response<ProblemDetail> responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    var title = responseBody.getContent().getTitle();
    assertThat(title).isEqualTo(apiException.getBody().getTitle());
  }

  @ParameterizedTest
  @MethodSource("apiExceptions")
  void responseBodyDetailIsEqualToExceptionBodyDetail(ApiException apiException) {
    ResponseEntity<Response<ProblemDetail>> response =
        apiExceptionAdvice.handleException(request, apiException);

    Response<ProblemDetail> responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    var detail = responseBody.getContent().getDetail();
    assertThat(detail).isEqualTo(apiException.getBody().getDetail());
  }

  @Test
  void responseBodyPropertiesCauseIsEqualToExceptionCauseLocalizedMessage() {
    ResponseEntity<Response<ProblemDetail>> response =
        apiExceptionAdvice.handleException(request, apiExceptionWithCause);

    Response<ProblemDetail> responseBody = response.getBody();
    assertThat(responseBody).isNotNull();
    Map<String, Object> properties = responseBody.getContent().getProperties();
    assertThat(properties).hasSize(2);
    @SuppressWarnings("unchecked")
    List<ProblemDetailUtility.ExceptionCauseDetail> causes =
        (List<ProblemDetailUtility.ExceptionCauseDetail>) properties.get("causes");

    assertThat(causes).isNotNull().hasSize(1);
    assertThat(causes.get(0).localizedMessage())
        .isEqualTo(apiExceptionWithCause.getCause().getLocalizedMessage());
  }
}
