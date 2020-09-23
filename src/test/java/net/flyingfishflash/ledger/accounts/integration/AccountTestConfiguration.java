package net.flyingfishflash.ledger.accounts.integration;

import net.flyingfishflash.ledger.accounts.AccountConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Integration Test Configuration

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
      "net.flyingfishflash.ledger.accounts",
      "net.flyingfishflash.ledger.foundation",
    })
public class AccountTestConfiguration extends AccountConfiguration {}
