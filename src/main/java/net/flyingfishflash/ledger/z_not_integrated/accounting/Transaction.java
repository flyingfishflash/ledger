package net.flyingfishflash.ledger.z_not_integrated.accounting;

import javax.annotation.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a group of related account entries.
 */
public class Transaction {
    final private Set<AccountingEntry> entries;

    final private long bookingDateTimestamp;

    //@Getter
    final private Map<String, String> info;
    
    public Map<String, String> getInfo() {
    	return info;
    }

    public Transaction(Set<AccountingEntry> entries,
                                 @Nullable Map<String, String> info,
                                 long bookingDateTimestamp) {
        if (info == null) info = new HashMap<>();
        this.info = info;
        this.entries = checkNotNull(entries);
        this.bookingDateTimestamp = bookingDateTimestamp;
        checkArgument(!entries.isEmpty());
        checkArgument(entries.size() >= 2, "A transaction consists of at least two entries");
        checkArgument(isBalanced(), "Transaction unbalanced");
        entries.forEach(e -> e.setTransaction(this));
    }

    public Transaction(Set<AccountingEntry> entries, Map<String, String> info) {
        this(entries, info, Instant.now().toEpochMilli());
    }

    public Transaction(Set<AccountingEntry> entries) {
        this(entries, null, Instant.now().toEpochMilli());
    }

    public boolean isBalanced() {
        BigDecimal debits = entries.stream().map(e -> e.getAccountSide() == AccountSide.DEBIT ?
                e.getAmount() : e.getAmount().negate()).reduce(BigDecimal.ZERO, BigDecimal::add);
        return debits.compareTo(BigDecimal.ZERO) == 0;
    }

    public List<AccountingEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ")
                .append(Instant.ofEpochMilli(bookingDateTimestamp).toString())
                .append("\n");
        entries.stream().forEach(e -> sb.append(e).append("\n"));
        return sb.toString();
    }
}
