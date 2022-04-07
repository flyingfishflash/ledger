package net.flyingfishflash.ledger.prices.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import net.flyingfishflash.ledger.prices.data.Price;
import net.flyingfishflash.ledger.prices.data.PriceRepository;
import net.flyingfishflash.ledger.prices.exceptions.PriceNotFoundException;
import net.flyingfishflash.ledger.prices.service.PriceService;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTests {

  @Mock private PriceRepository priceRepositoryMock;
  @InjectMocks private PriceService priceService;

  @Test
  public void testNewPrice() {
    Price price = priceService.newPrice();
    assertThat(price.getGuid()).isNotNull();
    StringBuilder sbr = new StringBuilder(price.getGuid());
    for (int i = 8, j = 0; i <= 20; i += 4, j++) sbr.insert(i + j, '-');
    // assertThat(UUID.fromString(sbr.toString()));
  }

  @Test
  public void testSavePrice() {
    priceService.savePrice(new Price());
    verify(priceRepositoryMock, times(1)).save(any(Price.class));
  }

  @Test
  public void testSaveAllPrices() {
    List<Price> priceList = Collections.singletonList(new Price());
    priceService.saveAllPrices(priceList);
    verify(priceRepositoryMock, times(1)).saveAll(anyList());
  }

  @Test
  public void testUpdatePrice() {
    priceService.updatePrice(new Price());
    verify(priceRepositoryMock, times(1)).save(any(Price.class));
  }

  @Test
  public void testDeletePrice() {
    priceService.deletePrice(1L);
    verify(priceRepositoryMock, times(1)).deleteById(anyLong());
  }

  @Test
  public void testDeleteAllCommodities() {
    priceService.deleteAllPrices();
    verify(priceRepositoryMock, times(1)).deleteAll();
  }

  @Test
  public void testFindById() {
    given(priceRepositoryMock.findById(anyLong())).willReturn(Optional.of(new Price()));
    priceService.findById(1L);
    verify(priceRepositoryMock, times(1)).findById(anyLong());
  }

  @Test
  public void testFindById_PriceNotFoundException() {

    Throwable exception =
        assertThrows(
            PriceNotFoundException.class,
            () -> {
              priceService.findById(1L);
            });

    verify(priceRepositoryMock, times(1)).findById(anyLong());
    System.out.println(exception.toString());
  }

  @Test
  public void testFindByGuid() {
    given(priceRepositoryMock.findByGuid(anyString())).willReturn(Optional.of(new Price()));
    priceService.findByGuid("any string");
    verify(priceRepositoryMock, times(1)).findByGuid(anyString());
  }

  @Test
  public void testFindByGuid_PriceNotFoundException() {

    Throwable exception =
        assertThrows(
            PriceNotFoundException.class,
            () -> {
              priceService.findByGuid("Any Guid");
            });

    verify(priceRepositoryMock, times(1)).findByGuid(anyString());
    System.out.println(exception.toString());
  }
}
