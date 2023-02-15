package net.flyingfishflash.ledger.unit.domain.accounts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.PrevSiblingAccountNotFoundException;

class AccountExceptionTests {

  private static class TestAccountException extends AbstractApiException {

    private TestAccountException() {
      super(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Title: Test General Account Exception",
          "Detail: Test General Account Exception");
    }
  }

  @Test
  void generalAccountException_getHttpStatus_isEqualTo500() {
    var testGeneralAccountException = new TestAccountException();
    assertThat(testGeneralAccountException.getStatusCode())
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void accountCreateException_getHttpsStatus_isEqualto400() {
    var accountCreateException = new AccountCreateException("aaa");
    assertThat(accountCreateException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void accountNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException("Any Guid");
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void accountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void accountNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException("any guid", "Exception Context");
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void eligibleParentAccountNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var accountException = new EligibleParentAccountNotFoundException("guid");
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void eligibleParentAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new EligibleParentAccountNotFoundException(1L);
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void nextSiblingAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new NextSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void prevSiblingAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new PrevSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
