package net.flyingfishflash.ledger.domain.importer.adapter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import net.flyingfishflash.ledger.domain.commodities.data.Commodity;
import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.dto.GncCommodity;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;

@Component
public class CommodityAdapter {

  private static final Logger logger = LoggerFactory.getLogger(CommodityAdapter.class);

  private final CommodityService commodityService;
  private GnucashFileImportStatus gnucashFileImportStatus;
  private ImportingBook importingBook;

  /**
   * Class constructor.
   *
   * <p>Translates GncCommodity objects to Commodity objects and persists the results.
   *
   * @param commodityService Service class for interacting with commodities
   * @param gnucashFileImportStatus Class representing status of Gnucash file import
   */
  public CommodityAdapter(
      CommodityService commodityService,
      GnucashFileImportStatus gnucashFileImportStatus,
      ImportingBook importingBook) {

    this.commodityService = commodityService;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
    this.importingBook = importingBook;
  }

  /**
   * Iterates over a GncCommodity collection, translating and persisting them.
   *
   * @param gncCommodities Collection of GncCommodities
   */
  public void addRecords(List<GncCommodity> gncCommodities) {

    List<Commodity> commodities = new ArrayList<>(gncCommodities.size());

    var templateCount = 0;
    var currencyCount = 0;

    for (GncCommodity gncCommodity : gncCommodities) {
      /* Exclude template and currency commodities */
      if (!gncCommodity.getNamespace().equalsIgnoreCase("template")
          && !gncCommodity.getNamespace().equalsIgnoreCase("currency")) {
        var commodity = commodityService.newCommodity(this.importingBook.getBook());
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

    gnucashFileImportStatus.setCommoditiesIgnoredTemplates(templateCount);
    gnucashFileImportStatus.setCommoditiesIgnoredCurrencies(currencyCount);
    gnucashFileImportStatus.setCommoditiesPersisted(0);

    logger.info("{} ignored templates", gnucashFileImportStatus.getCommoditiesIgnoredTemplates());
    logger.info("{} ignored currencies", gnucashFileImportStatus.getCommoditiesIgnoredCurrencies());

    var commodityCount = commodities.size();

    try {
      commodityService.saveAllCommodities(commodities);
    } catch (Exception exception) {
      commodityCount = 0;
      throw new ImportGnucashBookException(
          "Error While Saving Commodities", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      gnucashFileImportStatus.setCommoditiesPersisted(commodityCount);
      logger.info("{} persisted", gnucashFileImportStatus.getCommoditiesPersisted());
    }
  }
}
