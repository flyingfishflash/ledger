package net.flyingfishflash.ledger.domain.accounts.data;

public enum NormalBalance {
  DEBIT,
  CREDIT;

  static {
    DEBIT.opposite = CREDIT;
    CREDIT.opposite = DEBIT;
  }

  private NormalBalance opposite;

  public NormalBalance invert() {
    return opposite;
  }
}
