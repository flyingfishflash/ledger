package net.flyingfishflash.ledger.unit.domain.importer.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.accounts.service.AccountService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.adapter.TransactionAdapter;
import net.flyingfishflash.ledger.domain.importer.dto.GncTransaction;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;
import net.flyingfishflash.ledger.domain.transactions.service.TransactionService;

/** Unit tests for {@link net.flyingfishflash.ledger.domain.importer.adapter.TransactionAdapter} */
@ExtendWith(MockitoExtension.class)
class TransactionAdapterTests {

  @Mock private AccountService accountService;
  @Mock private GnucashFileImportStatus gnucashFileImportStatus;
  @Mock private ImportingBook importingBook;
  @Mock private TransactionService transactionService;
  @InjectMocks private TransactionAdapter transactionAdapter;

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaught() {
    var gncTransaction = new GncTransaction("Lorem Ipsum");
    gncTransaction.setCurrency("USD");
    var gncTransactionList = Collections.singletonList(gncTransaction);
    doThrow(RuntimeException.class)
        .when(transactionService)
        .saveAllTransactions(ArgumentMatchers.any());
    assertThatThrownBy(() -> transactionAdapter.addRecords(gncTransactionList))
        .isInstanceOf(ImportGnucashBookException.class);
  }
}
