package net.flyingfishflash.ledger.commodities.service;

import java.util.List;
import javax.transaction.Transactional;
import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.commodities.data.CommodityRepository;
import net.flyingfishflash.ledger.utilities.IdentifierFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    return commodityRepository.findById(commodityId).orElseThrow(RuntimeException::new);
  }

  public Commodity findByGuid(String guid) {

    return commodityRepository.findByGuid(guid).orElseThrow(RuntimeException::new);
  }

  public List<Commodity> findByMnemonic(String mnemonic) {

    return commodityRepository.findByMnemonic(mnemonic).orElseThrow(RuntimeException::new);
  }

  public List<Commodity> findByNameSpace(String nameSpace) {

    return commodityRepository.findByNameSpace(nameSpace).orElseThrow(RuntimeException::new);
  }

  public Commodity findByNameSpaceAndMnemonic(String nameSpace, String mnemonic) {

    return commodityRepository.findByNameSpaceAndMnemonic(nameSpace, mnemonic).orElseThrow(RuntimeException::new);
  }
}
