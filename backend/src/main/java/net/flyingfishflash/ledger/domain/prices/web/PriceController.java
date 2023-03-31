package net.flyingfishflash.ledger.domain.prices.web;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.domain.prices.service.PriceService;

@RestController
@Validated
@RequestMapping("/prices")
public class PriceController {

  @SuppressWarnings("unused")
  private final PriceService priceService;

  public PriceController(PriceService priceService) {

    this.priceService = priceService;
  }
}
