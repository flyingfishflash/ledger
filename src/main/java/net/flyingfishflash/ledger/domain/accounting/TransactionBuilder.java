package net.flyingfishflash.ledger.domain.accounting;

import javax.annotation.Nullable;

import net.flyingfishflash.ledger.domain.accounting.AccountSide;
import net.flyingfishflash.ledger.domain.accounting.AccountingEntry;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransactionBuilder {
    final private Set<AccountingEntry> entries = new HashSet<>();
    final private Map<String, String> info;

    private TransactionBuilder(@Nullable Map<String, String> info) {
        this.info = info;
    }

    public static TransactionBuilder create(@Nullable Map<String, String>  info) {
        return new TransactionBuilder(info);
    }

    public static TransactionBuilder create() {
        return new TransactionBuilder(null);
    }

    public TransactionBuilder debit(BigDecimal amount, String accountNumber) {
        entries.add(new AccountingEntry(amount, accountNumber, AccountSide.DEBIT));
        return this;
    }

    public TransactionBuilder credit(BigDecimal amount, String accountNumber) {
        entries.add(new AccountingEntry(amount, accountNumber, AccountSide.CREDIT));
        return this;
    }

    public Transaction build() {
        return new Transaction(entries, info, Instant.now().toEpochMilli());
    }
}
