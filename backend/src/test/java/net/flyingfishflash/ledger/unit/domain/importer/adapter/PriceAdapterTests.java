package net.flyingfishflash.ledger.unit.domain.importer.adapter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.commodities.data.Commodity;
import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.adapter.PriceAdapter;
import net.flyingfishflash.ledger.domain.importer.dto.GncPrice;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;
import net.flyingfishflash.ledger.domain.prices.data.Price;
import net.flyingfishflash.ledger.domain.prices.service.PriceService;

/** Unit tests for {@link net.flyingfishflash.ledger.domain.importer.adapter.PriceAdapter} */
@ExtendWith(MockitoExtension.class)
class PriceAdapterTests {

  @Mock private CommodityService commodityService;
  @Mock private PriceService priceService;
  @Mock private GnucashFileImportStatus gnucashFileImportStatus;
  @Mock private ImportingBook importingBook;
  @InjectMocks private PriceAdapter priceAdapter;

  @Test
  void addRecordsThrowsImportGnucashBookExceptionWhenServiceExceptionIsCaught() {
    var gncPrice = new GncPrice();
    gncPrice.setCommodityId("USD");
    gncPrice.setCurrencyId("USD");
    gncPrice.setPrice("9/99");
    var priceList = Collections.singletonList(gncPrice);
    var commodity = new Commodity();
    commodity.setFraction(1000);
    var price = new Price();
    price.setCommodity(commodity);
    price.setCurrency("USD");
    given(priceService.newPrice(importingBook.getBook())).willReturn(price);
    given(commodityService.findByBookAndNameSpaceAndMnemonic(any(), any(), any()))
        .willReturn(commodity);
    doThrow(RuntimeException.class).when(priceService).saveAllPrices(ArgumentMatchers.any());
    assertThatThrownBy(() -> priceAdapter.addRecords(priceList))
        .isInstanceOf(ImportGnucashBookException.class);
  }
}
