package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.GeneralAccountException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;

class AccountExceptionTests {

  private static class TestGeneralAccountException extends GeneralAccountException {

    private TestGeneralAccountException() {
      super("Test General Account Exception");
    }
  }

  @Test
  void generalAccountException_staticErrorDomain_isEqualToAccounts() {
    assertThat(TestGeneralAccountException.ERROR_DOMAIN).isEqualTo("Accounts");
  }

  @Test
  void generalAccountException_staticErrorSubject_isEqualToAccount() {
    assertThat(TestGeneralAccountException.ERROR_SUBJECT).isEqualTo("Account");
  }

  @Test
  void generalAccountException_getErrorDomain_isEqualToAccounts() {
    var accountException = new TestGeneralAccountException();
    assertThat(accountException.getErrorDomain()).isEqualTo("Accounts");
  }

  @Test
  void generalAccountException_getErrorSubject_isEqualToAccount() {
    var accountException = new TestGeneralAccountException();
    assertThat(accountException.getErrorSubject()).isEqualTo("Account");
  }

  @Test
  void generalAccountException_getHttpStatus_isEqualTo500() {
    var testGeneralAccountException = new TestGeneralAccountException();
    assertThat(testGeneralAccountException.getHttpStatus())
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void accountNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException("Any Guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void accountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void accountNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var accountException = new AccountNotFoundException("any guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void eligibleParentAccountNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var accountException = new EligibleParentAccountNotFoundException("guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void eligibleParentAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new EligibleParentAccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void eligibleParentAccountNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var accountException = new EligibleParentAccountNotFoundException("guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void nextSiblingAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new NextSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void prevSiblingAccountNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var accountException = new PrevSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
