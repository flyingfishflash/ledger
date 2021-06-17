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
  void testPriceException_getHttpStatus() {
    TestPriceException testPriceException = new TestPriceException();
    assertThat(testPriceException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void testPriceException_getErrorDomain() {
    TestPriceException testPriceException = new TestPriceException();
    assertThat(testPriceException.getErrorDomain()).isEqualTo("Prices");
  }

  @Test
  void testPriceException_getErrorSubject() {
    TestPriceException testPriceException = new TestPriceException();
    assertThat(testPriceException.getErrorSubject()).isEqualTo("Price");
  }

  @Test
  void testPriceException_getCause() {
    Exception illegalState = new IllegalStateException("Illegal State");
    TestPriceException testPriceException = new TestPriceException(illegalState);
    Throwable throwable = new Throwable(illegalState);
    assertThat(testPriceException).hasCause(illegalState);
  }

  @Test
  void testPriceNotFoundException_getHttpStatus1() {
    PriceNotFoundException priceException =
        new PriceNotFoundException("This is a Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testPriceNotFoundException_getHttpStatus2() {
    PriceNotFoundException priceException =
        new PriceNotFoundException(1L, "Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testPriceNotFoundException_getHttpStatus3() {
    PriceNotFoundException priceException =
        new PriceNotFoundException("Any Guid", "Price Not Found Exception");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testPriceNotFoundException_getHttpStatus4() {
    PriceNotFoundException priceException = new PriceNotFoundException(1L);
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testPriceCreateException_getHttpStatus1() {
    PriceCreateException priceException = new PriceCreateException("Message", new Exception());
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testPriceCreateException_getHttpStatus2() {
    PriceCreateException priceException = new PriceCreateException("Message");
    assertThat(priceException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
