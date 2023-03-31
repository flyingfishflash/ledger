package net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.factory;

import java.io.Serializable;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.DelegatingNestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control.QueryBasedNestedNodeInserter;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control.QueryBasedNestedNodeMover;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control.QueryBasedNestedNodeRebuilder;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control.QueryBasedNestedNodeRemover;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control.QueryBasedNestedNodeRetriever;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa.JpaNestedNodeInsertingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa.JpaNestedNodeMovingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa.JpaNestedNodeRebuildingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa.JpaNestedNodeRemovingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa.JpaNestedNodeRetrievingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.lock.NoLock;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;

/** Factory class to construct new instances of JPA Tree Repositories. */
public final class JpaNestedNodeRepositoryFactory {

  private JpaNestedNodeRepositoryFactory() {}

  /**
   * Creates a new instance of NestedNodeRepository backed by JPA storage without any Repository
   * locking.
   *
   * @param configuration - JPA Repository configuration
   * @param <ID> - Nested Node Identifier Class
   * @param <N> - Nested Node Class
   * @return - a new instance of NestedNodeRepository backed by JPA storage
   */
  @SuppressWarnings("java:S119")
  public static <ID extends Serializable, N extends NestedNode<ID>>
      NestedNodeRepository<ID, N> create(
          JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    return create(configuration, new NoLock<>());
  }

  /**
   * Creates a new instance of NestedNodeRepository backed by JPA storage with custom Repository
   * locking.
   *
   * @param configuration - JPA Repository configuration
   * @param lock - custom Repository Lock implementation
   * @param <ID> - Nested Node Identifier Class
   * @param <N> - Nested Node Class
   * @return - a new instance of NestedNodeRepository backed by JPA storage
   */
  @SuppressWarnings("java:S119")
  public static <ID extends Serializable, N extends NestedNode<ID>>
      NestedNodeRepository<ID, N> create(
          JpaNestedNodeRepositoryConfiguration<ID, N> configuration,
          NestedNodeRepository.Lock<ID, N> lock) {
    QueryBasedNestedNodeInserter<ID, N> inserter =
        new QueryBasedNestedNodeInserter<>(
            new JpaNestedNodeInsertingQueryDelegate<>(configuration));
    QueryBasedNestedNodeRetriever<ID, N> retriever =
        new QueryBasedNestedNodeRetriever<>(
            new JpaNestedNodeRetrievingQueryDelegate<>(configuration));
    return new DelegatingNestedNodeRepository<>(
        new QueryBasedNestedNodeMover<>(new JpaNestedNodeMovingQueryDelegate<>(configuration)),
        new QueryBasedNestedNodeRemover<>(new JpaNestedNodeRemovingQueryDelegate<>(configuration)),
        retriever,
        new QueryBasedNestedNodeRebuilder<>(
            inserter, retriever, new JpaNestedNodeRebuildingQueryDelegate<>(configuration)),
        inserter,
        lock);
  }
}
