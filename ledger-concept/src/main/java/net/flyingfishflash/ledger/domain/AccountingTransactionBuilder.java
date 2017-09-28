package net.flyingfishflash.ledger.domain;

import net.flyingfishflash.ledger.domain.AccountSide;
import net.flyingfishflash.ledger.domain.AccountingEntry;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccountingTransactionBuilder {
    final private Set<AccountingEntry> entries = new HashSet<>();
    final private Map<String, String> info;

    private AccountingTransactionBuilder(@Nullable Map<String, String> info) {
        this.info = info;
    }

    public static AccountingTransactionBuilder create(@Nullable Map<String, String>  info) {
        return new AccountingTransactionBuilder(info);
    }

    public static AccountingTransactionBuilder create() {
        return new AccountingTransactionBuilder(null);
    }

    public AccountingTransactionBuilder debit(BigDecimal amount, String accountNumber) {
        entries.add(new AccountingEntry(amount, accountNumber, AccountSide.DEBIT));
        return this;
    }

    public AccountingTransactionBuilder credit(BigDecimal amount, String accountNumber) {
        entries.add(new AccountingEntry(amount, accountNumber, AccountSide.CREDIT));
        return this;
    }

    public AccountingTransaction build() {
        return new AccountingTransaction(entries, info, Instant.now().toEpochMilli());
    }
}
