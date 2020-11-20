package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.accounts.exceptions.AccountException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.NextSiblingAccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.PrevSiblingAccountNotFoundException;

public class AccountExceptionTests {

  private static class TestAccountException extends AccountException {

    private TestAccountException() {
      super("Test Account Exception");
    }
  }

  @Test
  public void testAccountException_getHttpStatus() {
    TestAccountException testAccountException = new TestAccountException();
    assertThat(testAccountException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void testAccountException_getErrorDomain() {
    TestAccountException testAccountException = new TestAccountException();
    assertThat(testAccountException.getErrorDomain()).isEqualTo("Accounts");
  }

  @Test
  public void testAccountException_getErrorSubject() {
    TestAccountException testAccountException = new TestAccountException();
    assertThat(testAccountException.getErrorSubject()).isEqualTo("Account");
  }

  @Test
  public void testAccountNotFoundException_getHttpStatus1() {
    AccountNotFoundException accountException = new AccountNotFoundException("Account Guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testAccountNotFoundException_getHttpStatus2() {
    AccountNotFoundException accountException =
        new AccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testAccountNotFoundException_getHttpStatus3() {
    AccountNotFoundException accountException =
        new AccountNotFoundException("any guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus1() {
    EligibleParentAccountNotFoundException accountException =
        new EligibleParentAccountNotFoundException("guid");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus2() {
    EligibleParentAccountNotFoundException accountException =
        new EligibleParentAccountNotFoundException(1L, "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus3() {
    EligibleParentAccountNotFoundException accountException =
        new EligibleParentAccountNotFoundException("guid", "Exception Context");
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testNextSiblingAccountNotFoundException_getHttpsStatus1() {
    NextSiblingAccountNotFoundException accountException =
        new NextSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testPrevSiblingAccountNotFoundException_getHttpsStatus1() {
    PrevSiblingAccountNotFoundException accountException =
        new PrevSiblingAccountNotFoundException("Account Long Name", 1L);
    assertThat(accountException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
