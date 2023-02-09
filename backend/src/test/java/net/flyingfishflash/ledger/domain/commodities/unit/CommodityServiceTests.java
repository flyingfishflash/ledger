package net.flyingfishflash.ledger.domain.commodities.unit;

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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.commodities.data.Commodity;
import net.flyingfishflash.ledger.domain.commodities.data.CommodityRepository;
import net.flyingfishflash.ledger.domain.commodities.exceptions.CommodityNotFoundException;
import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;

@ExtendWith(MockitoExtension.class)
class CommodityServiceTests {

  @Mock private CommodityRepository mockCommodityRepository;
  @InjectMocks private CommodityService commodityService;

  @Test
  void newCommodity() {
    var commodity = commodityService.newCommodity();
    assertThat(commodity.getGuid()).isNotNull();
    var sbr = new StringBuilder(commodity.getGuid());
    for (int i = 8, j = 0; i <= 20; i += 4, j++) sbr.insert(i + j, '-');
    // assertThat(UUID.fromString(sbr.toString()));
  }

  @Test
  void saveCommodity() {
    commodityService.saveCommodity(new Commodity());
    verify(mockCommodityRepository, times(1)).save(any(Commodity.class));
  }

  @Test
  void saveAllCommodities() {
    var commodityList = Collections.singletonList(new Commodity());
    commodityService.saveAllCommodities(commodityList);
    verify(mockCommodityRepository, times(1)).saveAll(anyList());
  }

  @Test
  void updateCommodity() {
    commodityService.updateCommodity(new Commodity());
    verify(mockCommodityRepository, times(1)).save(any(Commodity.class));
  }

  @Test
  void deleteCommodity() {
    commodityService.deleteCommodity(1L);
    verify(mockCommodityRepository, times(1)).deleteById(anyLong());
  }

  @Test
  void deleteAllCommodities() {
    commodityService.deleteAllCommodities();
    verify(mockCommodityRepository, times(1)).deleteAll();
  }

  @Test
  void findById() {
    given(mockCommodityRepository.findById(anyLong())).willReturn(Optional.of(new Commodity()));
    commodityService.findById(1L);
    verify(mockCommodityRepository, times(1)).findById(anyLong());
  }

  @Test
  void findById_whenCommidityNotFound_thenCommodityNotFoundException() {
    assertThatExceptionOfType(CommodityNotFoundException.class)
        .isThrownBy(() -> commodityService.findById(1L));
    verify(mockCommodityRepository, times(1)).findById(anyLong());
  }

  @Test
  void findByGuid() {
    given(mockCommodityRepository.findByGuid(anyString())).willReturn(Optional.of(new Commodity()));
    commodityService.findByGuid("any string");
    verify(mockCommodityRepository, times(1)).findByGuid(anyString());
  }

  @Test
  void findByGuid_whenCommodityNotFound_thenCommodityNotFoundException() {
    assertThatExceptionOfType(CommodityNotFoundException.class)
        .isThrownBy(() -> commodityService.findByGuid("Any Guid"));
    verify(mockCommodityRepository, times(1)).findByGuid(anyString());
  }

  @Test
  void findByMnemonic() {
    var commodityList = Collections.singletonList(new Commodity());
    given(mockCommodityRepository.findByMnemonic(anyString()))
        .willReturn(Optional.of(commodityList));
    commodityService.findByMnemonic("any string");
    verify(mockCommodityRepository, times(1)).findByMnemonic(anyString());
  }

  @Test
  void findByMnemonic_whenCommodityNotFound_thenCommodityNotFoundException() {
    assertThatExceptionOfType(CommodityNotFoundException.class)
        .isThrownBy(() -> commodityService.findByMnemonic("Any String"));
    verify(mockCommodityRepository, times(1)).findByMnemonic(anyString());
  }

  @Test
  void findByNamespace() {
    var commodityList = Collections.singletonList(new Commodity());
    given(mockCommodityRepository.findByNamespace(anyString()))
        .willReturn(Optional.of(commodityList));
    commodityService.findByNameSpace("any string");
    verify(mockCommodityRepository, times(1)).findByNamespace(anyString());
  }

  @Test
  void findByBookAndNameSpaceAndMnemonic() {
    given(
            mockCommodityRepository.findByBookAndNamespaceAndMnemonic(
                any(Book.class), anyString(), anyString()))
        .willReturn(Optional.of(new Commodity()));
    commodityService.findByBookAndNameSpaceAndMnemonic(new Book(), "any string 1", "any string 2");
    verify(mockCommodityRepository, times(1))
        .findByBookAndNamespaceAndMnemonic(any(Book.class), anyString(), anyString());
  }

  @Test
  void findAllCommodities() {
    commodityService.findAllCommodities();
    verify(mockCommodityRepository, times(1)).findAll();
  }
}
