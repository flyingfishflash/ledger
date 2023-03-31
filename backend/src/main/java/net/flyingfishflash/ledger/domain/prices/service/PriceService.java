package net.flyingfishflash.ledger.domain.prices.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.core.utilities.IdentifierUtility;
import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.prices.data.Price;
import net.flyingfishflash.ledger.domain.prices.data.PriceRepository;
import net.flyingfishflash.ledger.domain.prices.exceptions.PriceNotFoundException;

@Service
@Transactional
public class PriceService {

  private final PriceRepository priceRepository;

  public PriceService(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Price newPrice() {

    return new Price(IdentifierUtility.identifier());
  }

  public Price newPrice(Book book) {

    return new Price(IdentifierUtility.identifier(), book);
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
