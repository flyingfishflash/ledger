package net.flyingfishflash.ledger.prices.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.prices.service.PriceService;

@RestController
@Validated
@RequestMapping("api/v1/ledger/prices")
public class PriceController {

  private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

  private final PriceService priceService;

  public PriceController(PriceService priceService) {

    this.priceService = priceService;
  }
}
