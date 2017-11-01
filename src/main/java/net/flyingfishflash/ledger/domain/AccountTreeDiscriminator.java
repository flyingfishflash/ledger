package net.flyingfishflash.ledger.domain;

import net.flyingfishflash.ledger.domain.AccountNode;
import pl.exsio.nestedj.discriminator.TreeDiscriminatorImpl;

import java.util.HashMap;
import java.util.Map;

public class AccountTreeDiscriminator extends TreeDiscriminatorImpl<AccountNode> {


    public AccountTreeDiscriminator() {
        Map<String, ValueProvider> valueProviders = new HashMap<>();
        valueProviders.put("discriminator", new ValueProvider() {
            @Override
            public Object getDiscriminatorValue() {
                return "account";
            }
        });

        setValueProviders(valueProviders);
    }
}