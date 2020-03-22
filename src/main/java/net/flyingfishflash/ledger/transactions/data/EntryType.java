package net.flyingfishflash.ledger.transactions.data;

public enum EntryType {
  DEBIT,
  CREDIT;

  private EntryType opposite;

  static {
    DEBIT.opposite = CREDIT;
    CREDIT.opposite = DEBIT;
  }

  public EntryType invert() {
    return opposite;
  }
}
