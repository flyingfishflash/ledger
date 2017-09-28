import net.flyingfishflash.ledger.domain.Journal;
import net.flyingfishflash.ledger.domain.Ledger;
import net.flyingfishflash.ledger.domain.ChartOfAccounts;
import net.flyingfishflash.ledger.domain.ChartOfAccountsBuilder;
import net.flyingfishflash.ledger.domain.AccountingTransaction;
import net.flyingfishflash.ledger.domain.AccountingTransactionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static net.flyingfishflash.ledger.domain.AccountSide.CREDIT;
import static net.flyingfishflash.ledger.domain.AccountSide.DEBIT;

public class LedgerTest {

    @Test
    public void testCreateLedgerWithExistingJournalAndCoa() {
        // Arrange
        String cashAccountNumber = "000001";
        String checkingAccountNumber = "000002";
        String liabilitiesAccountNumber = "000003";

        ChartOfAccounts coa = ChartOfAccountsBuilder.create()
                .addAccount(cashAccountNumber, "Cash", DEBIT)
                .addAccount(checkingAccountNumber, "Checking", DEBIT)
                .addAccount(liabilitiesAccountNumber, "Liabilities", CREDIT)
                .build();
        Journal journal = new Journal();

        // Deposit cash in the bank
        AccountingTransaction t1 = AccountingTransactionBuilder.create()
                .debit(new BigDecimal(222), checkingAccountNumber)
                .credit(new BigDecimal(222), cashAccountNumber)
                .build();
        // Get a loan
        AccountingTransaction t2 = AccountingTransactionBuilder.create()
                .debit(new BigDecimal(111), checkingAccountNumber)
                .credit(new BigDecimal(111), liabilitiesAccountNumber)
                .build();
        journal.addTransaction(t1);
        journal.addTransaction(t2);

        // Act
        Ledger ledger = new Ledger(journal, coa);

        //Assert
        Assert.assertEquals(new BigDecimal(-222), ledger.getAccountBalance(cashAccountNumber));
        Assert.assertEquals(new BigDecimal(333), ledger.getAccountBalance(checkingAccountNumber));
        Assert.assertEquals(new BigDecimal(111), ledger.getAccountBalance(liabilitiesAccountNumber));
    }

    @Test
    public void testJournalTransactionAccountMissingInCoa() {
        // TODO
    }

}
