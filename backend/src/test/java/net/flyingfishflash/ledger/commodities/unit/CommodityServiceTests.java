package net.flyingfishflash.ledger.commodities.unit;

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

import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.commodities.data.CommodityRepository;
import net.flyingfishflash.ledger.commodities.exceptions.CommodityNotFoundException;
import net.flyingfishflash.ledger.commodities.service.CommodityService;

@ExtendWith(MockitoExtension.class)
class CommodityServiceTests {

  @Mock private CommodityRepository commodityRepositoryMock;
  @InjectMocks private CommodityService commodityService;

  @Test
  void testNewCommodity() {
    Commodity commodity = commodityService.newCommodity();
    assertThat(commodity.getGuid()).isNotNull();
    StringBuilder sbr = new StringBuilder(commodity.getGuid());
    for (int i = 8, j = 0; i <= 20; i += 4, j++) sbr.insert(i + j, '-');
    // assertThat(UUID.fromString(sbr.toString()));
  }

  @Test
  void testSaveCommodity() {
    commodityService.saveCommodity(new Commodity());
    verify(commodityRepositoryMock, times(1)).save(any(Commodity.class));
  }

  @Test
  void testSaveAllCommodities() {
    List<Commodity> commodityList = Collections.singletonList(new Commodity());
    commodityService.saveAllCommodities(commodityList);
    verify(commodityRepositoryMock, times(1)).saveAll(anyList());
  }

  @Test
  void testUpdateCommodity() {
    commodityService.updateCommodity(new Commodity());
    verify(commodityRepositoryMock, times(1)).save(any(Commodity.class));
  }

  @Test
  void testDeleteCommodity() {
    commodityService.deleteCommodity(1L);
    verify(commodityRepositoryMock, times(1)).deleteById(anyLong());
  }

  @Test
  void testDeleteAllCommodities() {
    commodityService.deleteAllCommodities();
    verify(commodityRepositoryMock, times(1)).deleteAll();
  }

  @Test
  void testFindById() {
    given(commodityRepositoryMock.findById(anyLong())).willReturn(Optional.of(new Commodity()));
    commodityService.findById(1L);
    verify(commodityRepositoryMock, times(1)).findById(anyLong());
  }

  @Test
  void testFindById_CommodityNotFoundException() {

    Throwable exception =
        assertThrows(
            CommodityNotFoundException.class,
            () -> {
              commodityService.findById(1L);
            });

    verify(commodityRepositoryMock, times(1)).findById(anyLong());
    System.out.println(exception.toString());
  }

  @Test
  void testFindByGuid() {
    given(commodityRepositoryMock.findByGuid(anyString())).willReturn(Optional.of(new Commodity()));
    commodityService.findByGuid("any string");
    verify(commodityRepositoryMock, times(1)).findByGuid(anyString());
  }

  @Test
  void testFindByGuid_CommodityNotFoundException() {

    Throwable exception =
        assertThrows(
            CommodityNotFoundException.class,
            () -> {
              commodityService.findByGuid("Any Guid");
            });

    verify(commodityRepositoryMock, times(1)).findByGuid(anyString());
    System.out.println(exception.toString());
  }

  @Test
  void testFindByMnemonic() {
    List<Commodity> commodityList = Collections.singletonList(new Commodity());
    given(commodityRepositoryMock.findByMnemonic(anyString()))
        .willReturn(Optional.of(commodityList));
    commodityService.findByMnemonic("any string");
    verify(commodityRepositoryMock, times(1)).findByMnemonic(anyString());
  }

  @Test
  void testFindByMnemonic_CommodityNotFoundException() {

    Throwable exception =
        assertThrows(
            CommodityNotFoundException.class,
            () -> {
              commodityService.findByMnemonic("Any Mnemonic");
            });

    verify(commodityRepositoryMock, times(1)).findByMnemonic(anyString());
    System.out.println(exception.toString());
  }

  @Test
  void testFindByNamespace() {
    List<Commodity> commodityList = Collections.singletonList(new Commodity());
    given(commodityRepositoryMock.findByNamespace(anyString()))
        .willReturn(Optional.of(commodityList));
    commodityService.findByNameSpace("any string");
    verify(commodityRepositoryMock, times(1)).findByNamespace(anyString());
  }

  @Test
  void testFindByNameSpaceAndMnemonic() {
    given(commodityRepositoryMock.findByNamespaceAndMnemonic(anyString(), anyString()))
        .willReturn(Optional.of(new Commodity()));
    commodityService.findByNameSpaceAndMnemonic("any string 1", "any string 2");
    verify(commodityRepositoryMock, times(1)).findByNamespaceAndMnemonic(anyString(), anyString());
  }

  @Test
  void testFindAllCommodities() {
    commodityService.findAllCommodities();
    verify(commodityRepositoryMock, times(1)).findAll();
  }
}
