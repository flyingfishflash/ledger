package net.flyingfishflash.ledger.accounts.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.exsio.nestedj.config.jpa.discriminator.JpaTreeDiscriminator;
import pl.exsio.nestedj.model.NestedNode;

@Component
public class AccountTreeDiscriminator<ID extends Serializable, N extends NestedNode<ID>>
    implements JpaTreeDiscriminator<ID, N> {

  /** ImportingBook is a request-scoped bean representing the book being imported. */
  @Autowired ImportingBook importingBook;

  @Override
  public List<Predicate> getPredicates(CriteriaBuilder cb, Root root) {
    Predicate p = cb.equal(root.get("book"), importingBook.getBook().getId());
    return Collections.singletonList(p);
  }
}
