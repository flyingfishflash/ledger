package net.flyingfishflash.ledger.prices.integration;

import net.flyingfishflash.ledger.prices.PriceConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Integration Test Configuration

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"net.flyingfishflash.ledger.prices"})
public class PriceTestConfiguration extends PriceConfiguration {}
