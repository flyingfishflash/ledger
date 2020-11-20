package net.flyingfishflash.ledger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  // future state should have this setting overlaid by a user or book configuration setting
  public static final CurrencyUnit defaultCurrency = Monetary.getCurrency("USD");
}
