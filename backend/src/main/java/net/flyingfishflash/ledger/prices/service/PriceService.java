package net.flyingfishflash.ledger.prices.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.foundation.IdentifierFactory;
import net.flyingfishflash.ledger.prices.data.Price;
import net.flyingfishflash.ledger.prices.data.PriceRepository;
import net.flyingfishflash.ledger.prices.exceptions.PriceNotFoundException;

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

  public void deletePrice(Long priceId) {

    priceRepository.deleteById(priceId);
  }

  public void deleteAllPrices() {

    priceRepository.deleteAll();
  }

  public Price findById(Long id) {

    return priceRepository.findById(id).orElseThrow(() -> new PriceNotFoundException(id));
  }

  public Price findByGuid(String guid) {

    return priceRepository.findByGuid(guid).orElseThrow(() -> new PriceNotFoundException(guid));
  }
}
