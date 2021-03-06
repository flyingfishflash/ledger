package net.flyingfishflash.ledger.commodities.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.commodities.data.CommodityRepository;
import net.flyingfishflash.ledger.commodities.exceptions.CommodityNotFoundException;
import net.flyingfishflash.ledger.foundation.IdentifierFactory;

@Service
@Transactional
public class CommodityService {

  private static final Logger logger = LoggerFactory.getLogger(CommodityService.class);

  private final CommodityRepository commodityRepository;

  public CommodityService(CommodityRepository commodityRepository) {
    this.commodityRepository = commodityRepository;
  }

  public Commodity newCommodity() {

    return new Commodity(IdentifierFactory.getInstance().generateIdentifier());
  }

  public Commodity saveCommodity(Commodity commodity) {

    return commodityRepository.save(commodity);
  }

  public void saveAllCommodities(List<Commodity> commodities) {

    commodityRepository.saveAll(commodities);
  }

  public Commodity updateCommodity(Commodity commodity) {

    return commodityRepository.save(commodity);
  }

  public void deleteCommodity(Long commodityId) {

    commodityRepository.deleteById(commodityId);
  }

  public void deleteAllCommodities() {

    commodityRepository.deleteAll();
  }

  public List<Commodity> findAllCommodities() {

    return (List<Commodity>) commodityRepository.findAll();
  }

  public Commodity findById(Long commodityId) {

    return commodityRepository
        .findById(commodityId)
        .orElseThrow(() -> new CommodityNotFoundException(commodityId));
  }

  public Commodity findByGuid(String guid) {

    return commodityRepository
        .findByGuid(guid)
        .orElseThrow(() -> new CommodityNotFoundException(guid));
  }

  public List<Commodity> findByMnemonic(String mnemonic) {

    return commodityRepository
        .findByMnemonic(mnemonic)
        .orElseThrow(() -> new CommodityNotFoundException(mnemonic));
  }

  public List<Commodity> findByNameSpace(String nameSpace) {

    return commodityRepository
        .findByNamespace(nameSpace)
        .orElseThrow(() -> new CommodityNotFoundException(nameSpace));
  }

  public Commodity findByNameSpaceAndMnemonic(String nameSpace, String mnemonic) {

    return commodityRepository
        .findByNamespaceAndMnemonic(nameSpace, mnemonic)
        .orElseThrow(() -> new CommodityNotFoundException(mnemonic));
  }
}
