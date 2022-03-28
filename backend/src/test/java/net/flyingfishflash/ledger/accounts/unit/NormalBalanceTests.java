package net.flyingfishflash.ledger.accounts.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.NormalBalance;

class NormalBalanceTests {

  @Test
  void testNormalBalanceInvert() {
    assertEquals(NormalBalance.CREDIT, NormalBalance.DEBIT.invert());
    assertEquals(NormalBalance.DEBIT, NormalBalance.CREDIT.invert());
  }
}
