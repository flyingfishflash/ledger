package net.flyingfishflash.ledger.importer.adapter;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import net.flyingfishflash.ledger.commodities.service.CommodityService;
import net.flyingfishflash.ledger.importer.dto.GncPrice;
import net.flyingfishflash.ledger.importer.dto.GnucashFileImportStatus;
import net.flyingfishflash.ledger.prices.data.Price;
import net.flyingfishflash.ledger.prices.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PriceAdapter {

  private static final Logger logger = LoggerFactory.getLogger(PriceAdapter.class);

  /** Service class for interacting with prices */
  private final PriceService priceService;

  /** Service class for interacting with commodities */
  private final CommodityService commodityService;

  private GnucashFileImportStatus gnucashFileImportStatus;

  private List<Price> prices;

  /**
   * Class constructor.
   *
   * <p>Translates GncPrice objects to Price objects and persists the results.
   *  @param priceService Service class for interacting with prices
   * @param commodityService Service class for interacting with commodities
   * @param gnucashFileImportStatus
   */
  public PriceAdapter(PriceService priceService, CommodityService commodityService,
      GnucashFileImportStatus gnucashFileImportStatus) {

    this.priceService = priceService;
    this.commodityService = commodityService;
    this.gnucashFileImportStatus = gnucashFileImportStatus;
  }

  /**
   * Iterates over a GncPrice collection, translating and persisting them.
   *
   * @param gncPrices Collection of GncPrices
   */
  public void addRecords(List<GncPrice> gncPrices) {

    String currencyMnemonic;

    /* Sort the accumulated prices by timestamp in ascending order */
    Comparator<GncPrice> gncPriceComparator =
        (comparing(GncPrice::getDate, Comparator.nullsFirst(Comparator.naturalOrder())));

    gncPrices.sort(gncPriceComparator);

    prices = new ArrayList<Price>(gncPrices.size());

    for (GncPrice gncPrice : gncPrices) {

      Price price = priceService.newPrice();
      price.setGuid(gncPrice.getGuid());
      price.setDate(gncPrice.getDate());
      price.setSource(gncPrice.getSource());
      price.setType(gncPrice.getType());

      price.setCommodity(
          commodityService.findByNameSpaceAndMnemonic(
              gncPrice.getCommodityNamespace(), gncPrice.getCommodityId()));

      /* Attempt to set the currency */
      currencyMnemonic = gncPrice.getCurrencyId();
      try {
        String currency = Monetary.getCurrency(currencyMnemonic).toString();
        price.setCurrency(currency);
      } catch (UnknownCurrencyException e) {
        /* TODO: Throw GncImportException with UnknownCurrencyException as the cause */
        logger.info(e.getMessage());
        throw new UnknownCurrencyException(currencyMnemonic);
      }

      /* Derive the numerator and denominator fields */
      /* TODO error check splitting this string into an array of strings */
      String[] value = gncPrice.getPrice().split("/");
      price.setFraction(Long.parseLong(value[0]), Long.parseLong(value[1]));

      prices.add(price);
    }

    priceService.saveAllPrices(prices);
    logger.info(prices.size() + " persisted");
    gnucashFileImportStatus.setPricesPersisted(prices.size());
    prices = null;
  }
}
