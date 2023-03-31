package net.flyingfishflash.ledger.unit.domain.importer.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;

class ImporterExceptionTests {

  @Test
  void exceptionIsValidWhenDetailProvided() {
    var z = new ImportGnucashBookException("Lorem Ipsum");
    assertThat(z.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(z.getBody().getDetail()).isEqualTo("Lorem Ipsum");
  }

  @Test
  void exceptionIsValidWhenDetailAndCauseProvided() {
    var z =
        new ImportGnucashBookException(
            "Lorem Ipsum", new IllegalStateException("Test Illegal State Exception"));
    assertThat(z.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(z.getCause()).isInstanceOf(IllegalStateException.class);
    assertThat(z.getBody().getDetail()).isEqualTo("Lorem Ipsum");
  }

  @Test
  void exceptionIsValidWhenDetailAndCauseAndHttpStatusProvided() {
    var z =
        new ImportGnucashBookException(
            "Lorem Ipsum",
            new IllegalStateException("Test Illegal State Exception"),
            HttpStatus.I_AM_A_TEAPOT);
    assertThat(z.getStatusCode()).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
    assertThat(z.getCause()).isInstanceOf(IllegalStateException.class);
    assertThat(z.getBody().getDetail()).isEqualTo("Lorem Ipsum");
  }
}
