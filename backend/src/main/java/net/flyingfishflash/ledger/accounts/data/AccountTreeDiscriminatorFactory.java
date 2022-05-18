package net.flyingfishflash.ledger.accounts.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.books.data.Book;

public final class AccountTreeDiscriminatorFactory {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Book book) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("book", book::getId);
    return new MapJpaTreeDiscriminator<>(vp);
  }
}
