package net.flyingfishflash.ledger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.springframework.context.annotation.Configuration;

@SuppressWarnings("java:S1118")
@Configuration
public class ApplicationConfiguration {

  public static final String LOGGER_PREFIX = "🞛";
  public static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency("USD");
}
