import com.google.common.collect.Sets;

import net.flyingfishflash.ledger.z_not_integrated.accounting.AccountingEntry;
import net.flyingfishflash.ledger.z_not_integrated.accounting.Transaction;

import org.junit.Test;

import static net.flyingfishflash.ledger.z_not_integrated.accounting.AccountSide.CREDIT;
import static net.flyingfishflash.ledger.z_not_integrated.accounting.AccountSide.DEBIT;

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
