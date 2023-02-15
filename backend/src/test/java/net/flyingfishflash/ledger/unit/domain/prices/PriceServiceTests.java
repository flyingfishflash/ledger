package net.flyingfishflash.ledger.unit.domain.prices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.prices.data.Price;
import net.flyingfishflash.ledger.domain.prices.data.PriceRepository;
import net.flyingfishflash.ledger.domain.prices.exceptions.PriceNotFoundException;
import net.flyingfishflash.ledger.domain.prices.service.PriceService;

@ExtendWith(MockitoExtension.class)
class PriceServiceTests {

  @Mock private PriceRepository mockPriceRepository;
  @InjectMocks private PriceService priceService;

  @Test
  void newPrice() {
    Price price = priceService.newPrice();
    assertThat(price.getGuid()).isNotNull();
    StringBuilder sbr = new StringBuilder(price.getGuid());
    for (int i = 8, j = 0; i <= 20; i += 4, j++) sbr.insert(i + j, '-');
    // assertThat(UUID.fromString(sbr.toString()));
  }

  @Test
  void savePrice() {
    priceService.savePrice(new Price());
    verify(mockPriceRepository, times(1)).save(any(Price.class));
  }

  @Test
  void saveAllPrices() {
    List<Price> priceList = Collections.singletonList(new Price());
    priceService.saveAllPrices(priceList);
    verify(mockPriceRepository, times(1)).saveAll(anyList());
  }

  @Test
  void updatePrice() {
    priceService.updatePrice(new Price());
    verify(mockPriceRepository, times(1)).save(any(Price.class));
  }

  @Test
  void deletePrice() {
    priceService.deletePrice(1L);
    verify(mockPriceRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void deleteAllCommodities() {
    priceService.deleteAllPrices();
    verify(mockPriceRepository, times(1)).deleteAll();
  }

  @Test
  void findById() {
    given(mockPriceRepository.findById(anyLong())).willReturn(Optional.of(new Price()));
    priceService.findById(1L);
    verify(mockPriceRepository, times(1)).findById(1L);
  }

  @Test
  void findById_whenPriceNotFound_thenPriceNotFoundException() {
    assertThatExceptionOfType(PriceNotFoundException.class)
        .isThrownBy(() -> priceService.findById(1L));
    verify(mockPriceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findByGuid() {
    given(mockPriceRepository.findByGuid(anyString())).willReturn(Optional.of(new Price()));
    priceService.findByGuid("any string");
    verify(mockPriceRepository, times(1)).findByGuid(anyString());
  }

  @Test
  void findByGuid_whenPriceNotFound_thenPriceNotFoundException() {
    assertThatExceptionOfType(PriceNotFoundException.class)
        .isThrownBy(() -> priceService.findByGuid("Any Guid"));
    verify(mockPriceRepository, times(1)).findByGuid(anyString());
  }
}
