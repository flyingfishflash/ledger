package net.flyingfishflash.ledger;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LedgerConfiguration {

  public static final CurrencyUnit defaultCurrency = Monetary.getCurrency("USD");
}
