package net.flyingfishflash.ledger.unit.core.response.advice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import net.flyingfishflash.ledger.core.response.advice.MethodArgumentTypeMismatchExceptionAdvice;
import net.flyingfishflash.ledger.core.response.structure.Response;

/** Unit tests for {@link MethodArgumentTypeMismatchExceptionAdvice} */
@DisplayName("MethodArgumentTypeMismatchExceptionAdvice")
class MethodArgumentTypeMismatchExceptionAdviceTests {

  private static final MethodArgumentTypeMismatchExceptionAdvice
      methodArgumentTypeMismatchExceptionAdvice = new MethodArgumentTypeMismatchExceptionAdvice();

  private static final MockHttpServletRequest httpServletRequest =
      new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");

  private static final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException =
      new MethodArgumentTypeMismatchException(null, null, "Name", null, null);

  @Test
  void handleExceptionReturnsEssentiallyValidResponse() {
    ResponseEntity<?> response =
        methodArgumentTypeMismatchExceptionAdvice.handleException(
            httpServletRequest, methodArgumentTypeMismatchException);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isInstanceOf(Response.class);
  }
}
