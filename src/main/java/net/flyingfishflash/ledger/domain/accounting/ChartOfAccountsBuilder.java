package net.flyingfishflash.ledger.domain.accounting;

import java.util.HashSet;
import java.util.Set;

import net.flyingfishflash.ledger.domain.accounting.AccountDetails;
import net.flyingfishflash.ledger.domain.accounting.AccountDetailsImpl;
import net.flyingfishflash.ledger.domain.accounting.AccountSide;

public class ChartOfAccountsBuilder {
    private Set<AccountDetails> accountDetails = new HashSet<>();

    private ChartOfAccountsBuilder() {
    }

    public static ChartOfAccountsBuilder create() {
        return new ChartOfAccountsBuilder();
    }

    public ChartOfAccountsBuilder addAccount(String accountNumber, String name, AccountSide increaseSide) {
        AccountDetails accountDetails = new AccountDetailsImpl(accountNumber, name, increaseSide);
        this.accountDetails.add(accountDetails);
        return this;
    }

    public ChartOfAccounts build() {
        return new ChartOfAccounts(accountDetails);
    }
}
