import org.junit.Assert;
import org.junit.Test;

import net.flyingfishflash.ledger.z_not_integrated.accounting.AccountingEntry;
import net.flyingfishflash.ledger.z_not_integrated.accounting.Transaction;
import net.flyingfishflash.ledger.z_not_integrated.accounting.TransactionBuilder;

import java.math.BigDecimal;
import java.util.Objects;


public class AccountingTransactionBuilderTest {
    @Test
    public void testValidTransaction() {
        // Arrange
        String cashAccountNumber = "01";
        String checkingAccountNumber = "02";
        String liabilitiesAccountNumber = "11";

        // Act
        // We borrow money and get it part of it in cash, the rest as a wire transfer
        Transaction t = TransactionBuilder
                .create(null)
                .debit(new BigDecimal(10), cashAccountNumber)
                .debit(new BigDecimal(25), checkingAccountNumber)
                .credit(new BigDecimal(35), liabilitiesAccountNumber)
                .build();

        // Assert
        Assert.assertEquals(new BigDecimal(10), getEntryById(t, cashAccountNumber).getAmount());
        Assert.assertEquals(new BigDecimal(25), getEntryById(t, checkingAccountNumber).getAmount());
        Assert.assertEquals(new BigDecimal(35), getEntryById(t, liabilitiesAccountNumber).getAmount());
    }

    private AccountingEntry getEntryById(Transaction t, String accountId) {
        return t.getEntries().stream()
                .filter(e -> Objects.equals(e.getAccountNumber(), accountId))
                .findFirst().get();
    }
}
