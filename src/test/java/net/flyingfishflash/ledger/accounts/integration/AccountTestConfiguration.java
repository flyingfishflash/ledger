package net.flyingfishflash.ledger.accounts.integration;

import net.flyingfishflash.ledger.accounts.AccountConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @SpringBootConfiguration
// @EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
      "net.flyingfishflash.ledger.accounts",
      "net.flyingfishflash.ledger.foundation",
    })
public class AccountTestConfiguration extends AccountConfiguration {}
