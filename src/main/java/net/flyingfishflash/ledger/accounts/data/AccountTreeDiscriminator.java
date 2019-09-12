package net.flyingfishflash.ledger.accounts.data;

import java.util.HashMap;
import java.util.Map;
import net.flyingfishflash.ledger.accounts.data.Account;
import pl.exsio.nestedj.discriminator.MapTreeDiscriminator;

public class AccountTreeDiscriminator extends MapTreeDiscriminator<Long, Account> {

  public AccountTreeDiscriminator() {
    Map<String, ValueProvider> valueProviders = new HashMap<>();
    valueProviders.put("discriminator", () -> "account");
        /*{
            @Override
            public Object getDiscriminatorValue() {
                return "account";
            }
        });*/
    setValueProviders(valueProviders);
  }
}