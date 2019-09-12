/*
import com.google.common.collect.Sets;

import AccountingEntry;
import Transaction;

import org.junit.Test;

import static AccountSide.CREDIT;
import static AccountSide.DEBIT;

import java.math.BigDecimal;

public class AccountingEntryTest {

    @Test(expected = IllegalStateException.class)
    public void testFreeze() {
        // Arrange
        AccountingEntry entry1 = new AccountingEntry(new BigDecimal(20), "0001", DEBIT);
        AccountingEntry entry2 = new AccountingEntry(new BigDecimal(20), "0002", CREDIT);
        Transaction transaction = new Transaction(Sets.newHashSet(entry1, entry2));
        // Act + Assert
        entry1.setTransaction(transaction);
    }
}
*/
