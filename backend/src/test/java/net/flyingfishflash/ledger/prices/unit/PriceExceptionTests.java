package net.flyingfishflash.ledger.prices.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.prices.exceptions.GeneralPriceException;
import net.flyingfishflash.ledger.prices.exceptions.PriceCreateException;
import net.flyingfishflash.ledger.prices.exceptions.PriceNotFoundException;

class PriceExceptionTests {

  private static class TestPriceException extends GeneralPriceException {

    private TestPriceException() {
      super("Test Price Exception");
    }

    private TestPriceException(Exception cause) {
      super("Test Price Exception", cause);
    }
  }

  @Test
  void priceException_getErrorDomain_isEqualToPrices() {
    var testPriceException = new TestPriceException();
    assertThat(testPriceException.getErrorDomain()).isEqualTo("Prices");
  }

  @Test
  void priceException_getErrorSubject_isEqualToPrice() {
    var testPriceException = new TestPriceException();
    assertThat(testPriceException.getErrorSubject()).isEqualTo("Price");
  }

  @Test
  void priceException_getHttpStatus_isEqualTo501() {
    var testPriceException = new TestPriceException();
    assertThat(testPriceException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void priceExceptionWithExceptionParam_getCause_isEqualToConstructorParameter() {
    Exception illegalState = new IllegalStateException("Illegal State");
    var testPriceException = new TestPriceException(illegalState);
    assertThat(testPriceException).hasCause(illegalState);
  }

  @Test
  void priceNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var priceException = new PriceNotFoundException("This is a Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void priceNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var priceException = new PriceNotFoundException(1L, "Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void priceNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var priceException = new PriceNotFoundException("Any Guid", "Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void piceNotFoundExceptionWithLongParam_getHttpStatus_isEqualTo404() {
    var priceException = new PriceNotFoundException(1L);
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void priceCreateExceptionWithStringAndExceptionParams_getHttpStatus_isEqualTo400() {
    var priceException = new PriceCreateException("Message", new Exception());
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void priceCreateExceptionWithStringParam_getHttpStatus_isEqualTo400() {
    var priceException = new PriceCreateException("Message");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
