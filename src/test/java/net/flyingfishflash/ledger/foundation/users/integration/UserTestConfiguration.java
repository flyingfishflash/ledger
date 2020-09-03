package net.flyingfishflash.ledger.foundation.users.integration;

import net.flyingfishflash.ledger.foundation.users.UserConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Integration Test Configuration

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"net.flyingfishflash.ledger.foundation.users"})
public class UserTestConfiguration extends UserConfiguration {}
