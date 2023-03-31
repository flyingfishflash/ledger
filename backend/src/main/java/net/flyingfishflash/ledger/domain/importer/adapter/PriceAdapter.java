package net.flyingfishflash.ledger.domain.importer.adapter;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;
import net.flyingfishflash.ledger.domain.importer.ImportingBook;
import net.flyingfishflash.ledger.domain.importer.dto.GncPrice;
import net.flyingfishflash.ledger.domain.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.domain.importer.exceptions.ImportGnucashBookException;
import net.flyingfishflash.ledger.domain.prices.data.Price;
import net.flyingfishflash.ledger.domain.prices.service.PriceService;

@Component
public class PriceAdapter {

  private static final Logger logger = LoggerFactory.getLogger(PriceAdapter.class);

  private final CommodityService commodityService;
  private GnucashFileImportStatus gnucashFileImportStatus;
  private ImportingBook importingBook;
  private final PriceService priceService;

  /**
   * Class constructor.
   *
   * <p>Translates GncPrice objects to Price objects and persists the results.
   *
   * @param priceService Service class for interacting with prices
   * @param commodityService Service class for interacting with commodities
   * @param gnucashFileImportStatus class capturing the gnucash xml import status
   */
  public PriceAdapter(
      CommodityService commodityService,
      GnucashFileImportStatus gnucashFileImportStatus,
      ImportingBook importingBook,
      PriceService priceService) {

    this.commodityService = commodityService;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
    this.importingBook = importingBook;
    this.priceService = priceService;
  }

  /**
   * Iterates over a GncPrice collection, translating and persisting them.
   *
   * @param gncPrices Collection of GncPrices
   */
  public void addRecords(List<GncPrice> gncPrices) {

    String currencyMnemonic;

    List<Price> prices;

    /* Sort the accumulated prices by timestamp in ascending order */
    Comparator<GncPrice> gncPriceComparator =
        comparing(GncPrice::getDate, Comparator.nullsFirst(Comparator.naturalOrder()));

    gncPrices.sort(gncPriceComparator);

    prices = new ArrayList<>(gncPrices.size());

    for (GncPrice gncPrice : gncPrices) {

      var price = priceService.newPrice(importingBook.getBook());
      price.setGuid(gncPrice.getGuid());
      price.setDate(gncPrice.getDate());
      price.setSource(gncPrice.getSource());
      price.setType(gncPrice.getType());

      price.setCommodity(
          commodityService.findByBookAndNameSpaceAndMnemonic(
              importingBook.getBook(),
              gncPrice.getCommodityNamespace(),
              gncPrice.getCommodityId()));

      /* Attempt to set the currency */
      currencyMnemonic = gncPrice.getCurrencyId();
      try {
        var currency = Monetary.getCurrency(currencyMnemonic).toString();
        price.setCurrency(currency);
      } catch (UnknownCurrencyException e) {
        /* TODO: Throw ImportGnucashBookException with UnknownCurrencyException as the cause */
        logger.info(e.getMessage());
        throw new UnknownCurrencyException(currencyMnemonic);
      }

      /* Derive the numerator and denominator fields */
      /* TODO error check splitting this string into an array of strings */
      String[] value = gncPrice.getPrice().split("/", -1);
      price.setFraction(Long.parseLong(value[0]), Long.parseLong(value[1]));

      prices.add(price);
    }

    var priceCount = prices.size();

    try {
      priceService.saveAllPrices(prices);
    } catch (Exception exception) {
      priceCount = 0;
      throw new ImportGnucashBookException(
          "Error While Saving Prices", exception, HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
      gnucashFileImportStatus.setPricesPersisted(priceCount);
      logger.info("{} persisted", gnucashFileImportStatus.getPricesPersisted());
    }
    // TODO verify if it's neccessary to null out the price list
    // prices = null;
  }
}
