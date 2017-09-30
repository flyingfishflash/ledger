import net.flyingfishflash.ledger.domain.AccountSide;
import net.flyingfishflash.ledger.domain.AccountingEntry;
import net.flyingfishflash.ledger.domain.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AccountingTransactionTest {

    @Test
    public void testBalanced() {
        // Arrange
        Set<AccountingEntry> entries = new HashSet<>();
        entries.add(new AccountingEntry(new BigDecimal(50), "Cash", AccountSide.DEBIT));
        entries.add(new AccountingEntry(new BigDecimal(50), "Liabilities", AccountSide.CREDIT));
        // Act + Assert
        Transaction t = new Transaction(entries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnbalanced() {
        // Arrange
        Set<AccountingEntry> entries = new HashSet<>();
        entries.add(new AccountingEntry(new BigDecimal(10), "Cash", AccountSide.DEBIT));
        entries.add(new AccountingEntry(new BigDecimal(50), "Liabilities", AccountSide.CREDIT));
        // Act + Assert
        Transaction t = new Transaction(entries);
    }

    public void testInfo() {
        // TODO
    }
}
