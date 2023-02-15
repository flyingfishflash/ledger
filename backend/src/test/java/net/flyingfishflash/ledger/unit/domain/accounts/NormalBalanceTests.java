package net.flyingfishflash.ledger.unit.domain.accounts;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.domain.accounts.data.NormalBalance;

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
