package net.flyingfishflash.ledger.accounts.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.accounts.data.NormalBalance;

class NormalBalanceTests {

  @Test
  void whenCreditInverted_then_Debit() {
    assertThat(NormalBalance.CREDIT.invert()).isEqualTo(NormalBalance.DEBIT);
  }

  @Test
  void whenDebitInverted_then_Credit() {
    assertThat(NormalBalance.DEBIT.invert()).isEqualTo(NormalBalance.CREDIT);
  }
}
