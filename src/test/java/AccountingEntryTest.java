import com.google.common.collect.Sets;
import net.flyingfishflash.ledger.domain.AccountingEntry;
import net.flyingfishflash.ledger.domain.Transaction;

import org.junit.Test;

import java.math.BigDecimal;

import static net.flyingfishflash.ledger.domain.AccountSide.CREDIT;
import static net.flyingfishflash.ledger.domain.AccountSide.DEBIT;

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
