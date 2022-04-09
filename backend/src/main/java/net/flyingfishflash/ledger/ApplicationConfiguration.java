package net.flyingfishflash.ledger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.springframework.context.annotation.Configuration;

@SuppressWarnings("java:S1118")
@Configuration
public class ApplicationConfiguration {

  // TODO: Deal with configurable application properties
  public static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("USD");
}
