package net.flyingfishflash.ledger.prices.web;

import net.flyingfishflash.ledger.prices.service.PriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("ledger/api/v1/prices")
public class PriceController {

  private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

  private final PriceService priceService;

  public PriceController(PriceService priceService) {

    this.priceService = priceService;
  }
}
