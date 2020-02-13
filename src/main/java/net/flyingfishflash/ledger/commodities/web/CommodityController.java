package net.flyingfishflash.ledger.commodities.web;

import net.flyingfishflash.ledger.commodities.service.CommodityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("ledger/api/v1/commodities")
public class CommodityController {

  private static final Logger logger = LoggerFactory.getLogger(CommodityController.class);

  private final CommodityService commodityService;

  public CommodityController(CommodityService commodityService) {

    this.commodityService = commodityService;
  }
}
