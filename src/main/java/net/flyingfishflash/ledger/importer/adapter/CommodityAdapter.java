package net.flyingfishflash.ledger.importer.adapter;

import java.util.ArrayList;
import java.util.List;
import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.commodities.service.CommodityService;
import net.flyingfishflash.ledger.importer.dto.GncCommodity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommodityAdapter {

  private static final Logger logger = LoggerFactory.getLogger(CommodityAdapter.class);

  /** Service class for interacting with commodities */
  private final CommodityService commodityService;

  /**
   * Class constructor.
   *
   * <p>Translates GncCommodity objects to Commodity objects and persists the results.
   *
   * @param commodityService Service class for interacting with commodities
   */
  public CommodityAdapter(CommodityService commodityService) {

    this.commodityService = commodityService;
  }

  /**
   * Iterates over a GncCommodity collection, translating and persisting them.
   *
   * @param gncCommodities Collection of GncCommodities
   */
  public void addRecords(List<GncCommodity> gncCommodities) {

    List<Commodity> commodities = new ArrayList<>(gncCommodities.size());

    int templateCount = 0;
    int currencyCount = 0;

    for (GncCommodity gncCommodity : gncCommodities) {
      /* Exclude template and currency commodities */
      if (!gncCommodity.getNamespace().equalsIgnoreCase("template")
          && !gncCommodity.getNamespace().equalsIgnoreCase("currency")) {
        Commodity commodity = commodityService.newCommodity();
        commodity.setMnemonic(gncCommodity.getMnemonic());
        commodity.setFullName(gncCommodity.getFullName());
        commodity.setNamespace(gncCommodity.getNamespace());
        commodity.setCusip(gncCommodity.getCusip());
        commodity.setFraction(gncCommodity.getSmallestFraction());
        commodity.setCustomIdentifier(gncCommodity.getLocalSymbol());
        commodity.setQuoteRemote(gncCommodity.getQuoteRemote());
        commodities.add(commodity);
      } else {
        if (gncCommodity.getNamespace().equalsIgnoreCase("template")) templateCount++;
        if (gncCommodity.getNamespace().equalsIgnoreCase("currency")) currencyCount++;
      }
    }
    commodityService.saveAllCommodities(commodities);
    logger.info(templateCount + " ignored templates");
    logger.info(currencyCount + " ignored currencies");
    logger.info(commodities.size() + " persisted");
  }
}
