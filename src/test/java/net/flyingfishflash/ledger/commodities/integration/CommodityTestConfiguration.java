package net.flyingfishflash.ledger.commodities.integration;

import net.flyingfishflash.ledger.commodities.CommodityConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Integration Test Configuration

// @SpringBootConfiguration
// @EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
      "net.flyingfishflash.ledger.commodities",
      "net.flyingfishflash.ledger.foundation",
    })
public class CommodityTestConfiguration extends CommodityConfiguration {}
