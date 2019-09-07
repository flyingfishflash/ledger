package not_integrated.accounting.accounting;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
//import lombok.Getter;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a set of accounts and their transactions.
 */
final public class Ledger {
    final private HashMap<String, Account> accountNumberToAccount = new HashMap<>();

    //@Getter
    final private Journal journal = new Journal();
    final private ChartOfAccounts coa;

    public Ledger(ChartOfAccounts coa) {
        this.coa = coa;
        // Create coa accounts
        coa.getAccountNumberToAccountDetails().values().forEach(this::addAccount);
    }

    public Ledger(Journal journal, ChartOfAccounts coa) {
        this(coa);
        // Add transactions
        journal.getTransactions().forEach(this::commitTransaction);
    }

    public TransactionBuilder createTransaction(@Nullable Map<String, String> info) {
        return TransactionBuilder.create(info);
    }

    public void commitTransaction(Transaction transaction) {
        // Add entries to accounts
        transaction.getEntries().forEach(this::addAccountEntry);
        journal.addTransaction(transaction);
    }

    public TrialBalanceResult computeTrialBalance() {
        return new TrialBalanceResult(Sets.newHashSet(accountNumberToAccount.values()));
    }

    public BigDecimal getAccountBalance(String accountNumber) {
        return accountNumberToAccount.get(accountNumber).getBalance();
    }
    
    public Journal getJournal() {
    	// may be issues with this, removed lombok
    	return journal;
    }

    private void addAccount(AccountDetails accountDetails) {
        String newAccountNumber = accountDetails.getAccountNumber();
        boolean accountNumberNotInUse = !accountNumberToAccount.containsKey(newAccountNumber);
        checkArgument(accountNumberNotInUse,
                "An account with the account number %s exists already in the ledger", newAccountNumber);
        accountNumberToAccount.put(accountDetails.getAccountNumber(), new Account(accountDetails));
    }

    private void addAccountEntry(AccountingEntry entry) {
        accountNumberToAccount.get(entry.getAccountNumber()).addEntry(entry);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("accountNumberToAccountMap", accountNumberToAccount)
                .add("journal", journal)
                .add("chartOfAccounts", coa)
                .toString();
    }
}
