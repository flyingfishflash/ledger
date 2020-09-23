package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.flyingfishflash.ledger.accounts.data.NormalBalance;
import org.junit.jupiter.api.Test;

public class NormalBalanceTests {

  @Test
  public void testNormalBalanceInvert() {
    assertEquals(NormalBalance.CREDIT, NormalBalance.DEBIT.invert());
    assertEquals(NormalBalance.DEBIT, NormalBalance.CREDIT.invert());
  }
}
