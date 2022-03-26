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
  void testGeneralAccountException_static_ErrorDomain() {
    assertThat(TestGeneralAccountException.ERROR_DOMAIN).isEqualTo("Accounts");
  }

  @Test
  void testGeneralAccountException_static_ErrorSubject() {
    assertThat(TestGeneralAccountException.ERROR_SUBJECT).isEqualTo("Account");
  }

  @Test
  void testGeneralAccountException_getErrorDomain() {
    var accountException = new TestGeneralAccountException();
    assertThat(accountException.getErrorDomain()).isEqualTo("Accounts");
  }

  @Test
  void testGeneralAccountException_getErrorSubject() {
    var accountException = new TestGeneralAccountException();
    assertThat(accountException.getErrorSubject()).isEqualTo("Account");
  }

  @Test
  void testAccountException_getHttpStatus() {
    var testGeneralAccountException = new TestGeneralAccountException();
    assertThat(testGeneralAccountException.getHttpStatus())
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void testAccountNotFoundException_getHttpStatus1() {
    var accountException = new AccountNotFoundException("any guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testAccountNotFoundException_getHttpStatus2() {
    var accountException = new AccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testAccountNotFoundException_getHttpStatus3() {
    var accountException = new AccountNotFoundException("any guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testEligibleParentAccountNotFoundException_getHttpStatus1() {
    var accountException = new EligibleParentAccountNotFoundException("guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testEligibleParentAccountNotFoundException_getHttpStatus2() {
    var accountException = new EligibleParentAccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testEligibleParentAccountNotFoundException_getHttpStatus3() {
    var accountException = new EligibleParentAccountNotFoundException("guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testNextSiblingAccountNotFoundException_getHttpsStatus1() {
    var accountException = new NextSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testPrevSiblingAccountNotFoundException_getHttpsStatus1() {
    var accountException = new PrevSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
