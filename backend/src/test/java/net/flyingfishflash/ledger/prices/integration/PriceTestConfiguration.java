package net.flyingfishflash.ledger.prices.integration;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.flyingfishflash.ledger.prices.PriceConfiguration;

// Integration Test Configuration

@Tag("Integration")
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
      "net.flyingfishflash.ledger.prices",
      "net.flyingfishflash.ledger.foundation",
    })
public class PriceTestConfiguration extends PriceConfiguration {}
