package net.flyingfishflash.ledger.commodities.integration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.flyingfishflash.ledger.commodities.CommodityConfiguration;

// Integration Test Configuration

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
      "net.flyingfishflash.ledger.commodities",
      "net.flyingfishflash.ledger.foundation",
    })
public class CommodityTestConfiguration extends CommodityConfiguration {}
