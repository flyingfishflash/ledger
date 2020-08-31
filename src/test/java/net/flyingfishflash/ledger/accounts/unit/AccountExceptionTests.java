package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.flyingfishflash.ledger.accounts.exceptions.AccountException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.accounts.exceptions.EligibleParentAccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AccountExceptionTests {

  @Test
  public void testAccountException_getErrorDomain() {
    AccountException accountException =
        new AccountNotFoundException("This is an account Exception");

    assertEquals("Accounts", accountException.getErrorDomain());
  }

  @Test
  public void testAccountException_getHttpStatus1() {
    AccountException accountException =
        new AccountNotFoundException("This is an account Exception");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }

  @Test
  public void testAccountException_getHttpStatus2() {

    AccountException accountException =
        new AccountNotFoundException(1L, "Account Not Found Exception");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }

  @Test
  public void testAccountException_getHttpStatus3() {

    AccountException accountException =
        new AccountNotFoundException("any guid", "Account Not Found Exception");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus1() {

    AccountException accountException =
        new EligibleParentAccountNotFoundException("guid");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus2() {

    AccountException accountException =
        new EligibleParentAccountNotFoundException(1L, "Account Name");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }

  @Test
  public void testEligibleParentAccountNotFoundException_getHttpStatus3() {

    AccountException accountException =
        new EligibleParentAccountNotFoundException("guid", "Account Name");

    assertEquals(HttpStatus.NOT_FOUND, accountException.getHttpStatus());
  }
}
