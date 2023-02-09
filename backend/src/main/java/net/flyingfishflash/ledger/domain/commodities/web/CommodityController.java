package net.flyingfishflash.ledger.domain.commodities.web;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.domain.commodities.service.CommodityService;

// @Tag(name = "commodity controller")
@RestController
@Validated
@RequestMapping("api/v1/ledger/commodities")
public class CommodityController {

  @SuppressWarnings("unused")
  private final CommodityService commodityService;

  public CommodityController(CommodityService commodityService) {

    this.commodityService = commodityService;
  }
}
