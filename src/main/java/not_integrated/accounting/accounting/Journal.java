package not_integrated.accounting.accounting;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a collection of transactions.
 */
public class Journal {
    final private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        checkNotNull(transaction);
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("transactions", transactions)
                .toString();
    }
}
