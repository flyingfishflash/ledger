package net.flyingfishflash.ledger.unit.domain.importer.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.commodities.data.Commodity;
import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.adapter.CommodityAdapter;
import net.flyingfishflash.ledger.domain.importer.dto.GncCommodity;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;

@ExtendWith(MockitoExtension.class)
class CommodityAdapterTests {

  @Mock private CommodityService commodityService;
  @Mock private GnucashFileImportStatus gnucashFileImportStatus;
  @Mock private ImportingBook importingBook;
  @InjectMocks private CommodityAdapter commodityAdapter;

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaught() {
    var gncCommodity = new GncCommodity();
    gncCommodity.setNamespace("Lorem Ipsum");
    gncCommodity.setMnemonic("BBY");
    var commodity = new Commodity();
    var gncCommodityList = Collections.singletonList(gncCommodity);
    when(commodityService.newCommodity(this.importingBook.getBook())).thenReturn(commodity);
    doThrow(RuntimeException.class)
        .when(commodityService)
        .saveAllCommodities(ArgumentMatchers.any());
    assertThatThrownBy(() -> commodityAdapter.addRecords(gncCommodityList))
        .isInstanceOf(ImportGnucashBookException.class);
  }

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaught1() {
    var gncCommodity = new GncCommodity();
    gncCommodity.setNamespace("Lorem Ipsum");
    gncCommodity.setMnemonic("BBY");
    var commodity = new Commodity();
    var gncCommodityList = Collections.singletonList(gncCommodity);
    when(commodityService.newCommodity(this.importingBook.getBook())).thenReturn(commodity);
    commodityAdapter.addRecords(gncCommodityList);
    Mockito.verify(commodityService, times(1)).saveAllCommodities(ArgumentMatchers.any());
  }
}
