package net.flyingfishflash.ledger.commodities;

import net.flyingfishflash.ledger.commodities.data.Commodity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackages = {
      "net.flyingfishflash.ledger.foundation",
      "net.flyingfishflash.ledger.commodities",
    })
@EntityScan(basePackageClasses = Commodity.class)
public class CommodityConfiguration {}
