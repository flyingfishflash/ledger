package net.flyingfishflash.ledger.unit.domain.importer.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.service.AccountService;
import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.adapter.AccountAdapter;
import net.flyingfishflash.ledger.domain.importer.dto.GncAccount;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;

@ExtendWith(MockitoExtension.class)
class AccountAdapterTests {

  @Mock private AccountService accountService;
  @Mock private CommodityService commodityService;
  @Mock private GnucashFileImportStatus gnucashFileImportStatus;
  @InjectMocks AccountAdapter accountAdapter;

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaughtForRootAccount() {
    var gncAccountRoot = new GncAccount("root");
    gncAccountRoot.setGncAccountType("root");
    gncAccountRoot.setGuid("Lorem Ipsum");
    var gncAccountList = List.of(gncAccountRoot);
    var book = new Book();
    book.setId(999L);
    var importingBook = new ImportingBook();
    importingBook.setBook(book);
    ReflectionTestUtils.setField(accountAdapter, "importingBook", importingBook);
    doThrow(RuntimeException.class).when(accountService).insertAsFirstRoot(any(Account.class));
    assertThatThrownBy(() -> accountAdapter.addRecords(gncAccountList))
        .isInstanceOf(ImportGnucashBookException.class)
        .hasMessageContaining("Error While Saving Root Account");
  }

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaughtForNonRootAccount() {
    var gncAccountRoot = new GncAccount("root");
    gncAccountRoot.setGncAccountType("root");
    gncAccountRoot.setGuid("Lorem Ipsum");
    var gncAccount = new GncAccount("asset account");
    gncAccount.setGncAccountType("ASSET");
    gncAccount.setGuid("Lorem Ipsum");
    gncAccount.setGncCommodityNamespace("NYCSTOCK");
    var gncAccountList = List.of(gncAccountRoot, gncAccount);
    var book = new Book();
    book.setId(999L);
    var importingBook = new ImportingBook();
    importingBook.setBook(book);
    ReflectionTestUtils.setField(accountAdapter, "importingBook", importingBook);
    when(accountService.findByGuid(any())).thenReturn(new Account("Lorem Ipsum"));
    accountAdapter.addRecords(gncAccountList);
    doThrow(RuntimeException.class)
        .when(accountService)
        .insertAsLastChildOf(
            ArgumentMatchers.any(Account.class), ArgumentMatchers.any(Account.class));
    assertThatThrownBy(() -> accountAdapter.addRecords(gncAccountList))
        .isInstanceOf(ImportGnucashBookException.class)
        .hasMessageContaining("Error While Saving Accounts");
  }
}
