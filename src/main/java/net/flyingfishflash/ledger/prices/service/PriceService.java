package net.flyingfishflash.ledger.prices.service;

import java.util.List;
import net.flyingfishflash.ledger.prices.data.Price;
import net.flyingfishflash.ledger.prices.data.PriceRepository;
import net.flyingfishflash.ledger.utilities.IdentifierFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PriceService {

  private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

  private final PriceRepository priceRepository;

  public PriceService(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Price newPrice() {

    return new Price(IdentifierFactory.getInstance().generateIdentifier().toString());
  }

  public Price savePrice(Price price) {

    return priceRepository.save(price);
  }

  public void saveAllPrices(List<Price> prices) {

    priceRepository.saveAll(prices);
  }

  public Price updatePrice(Price price) {

    return priceRepository.save(price);
  }

  public void deletePrice(Price price) {

    priceRepository.delete(price);
  }

  public void deleteAllPrices() {

    priceRepository.deleteAll();
  }

  public Price findById(Long id) {

    return priceRepository.findById(id).orElseThrow(RuntimeException::new);
  }

  public Price findByGuid(String guid) {

    return priceRepository.findByGuid(guid).orElseThrow(RuntimeException::new);
  }
}
