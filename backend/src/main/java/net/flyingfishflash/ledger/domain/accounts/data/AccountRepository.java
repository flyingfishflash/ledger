package net.flyingfishflash.ledger.domain.accounts.data;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.NestedNodeRepository;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.domain.accounts.exceptions.AccountNotFoundException;
import net.flyingfishflash.ledger.domain.books.data.Book;

@Repository
@Transactional
public class AccountRepository {

  @PersistenceContext private EntityManager entityManager;

  private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);
  private final NestedNodeRepository<Long, Account> nodeRepository;

  public AccountRepository(NestedNodeRepository<Long, Account> nodeRepository) {
    this.nodeRepository = nodeRepository;
  }

  public Account newAccount(String guid) {
    return new Account(guid);
  }

  //  private static <ID extends Serializable, N extends NestedNode<ID>>
  //      JpaTreeDiscriminator<ID, N> getDiscriminator(Book book) {
  //    Map<String, Supplier<Object>> vp = new HashMap<>();
  //    vp.put("book", book::getId);
  //    return new MapJpaTreeDiscriminator<>(vp);
  //  }

  @SuppressWarnings("unused")
  private void save(Account account) {

    preventUnsafeChanges(account);
    entityManager.persist(account);
  }

  public void update(Account account, Book book) {

    preventUnsafeChanges(account);

    // derive and set the longName on all child accounts of the subject account
    Iterable<Account> t =
        this.nodeRepository.getTreeAsList(
            account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
    Iterator<Account> it = t.iterator();
    while (it.hasNext()) {
      Account next = it.next();
      next.setLongName(this.deriveLongName(next, book));
    }
    entityManager.merge(account);
  }

  public Optional<Account> findRoot() {

    try {
      var cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Account> query = cb.createQuery(Account.class);
      Root<Account> root = query.from(Account.class);
      query.where(cb.isNull(root.get("parentId")));
      return Optional.of(entityManager.createQuery(query).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public Optional<Account> findById(Long id) {

    try {
      var cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Account> select = cb.createQuery(Account.class);
      Root<Account> root = select.from(Account.class);
      select.where(cb.equal(root.get("id"), id));
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public Optional<Account> findByGuid(String guid) {

    try {
      var cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Account> select = cb.createQuery(Account.class);
      Root<Account> root = select.from(Account.class);
      select.where(cb.equal(root.get("guid"), guid));
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public Iterable<Account> getTreeAsList(Account account, Book book) {

    return this.nodeRepository.getTreeAsList(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public String deriveLongName(Account account, Book book) {

    Account parent =
        this.findById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify parent account of an account for which we are deriving the long name."));

    if (parent.getTreeLeft() <= 1) {
      return account.getName();
    }

    List<Account> parents =
        this.nodeRepository.getParents(
            parent, AccountTreeDiscriminatorFactory.getDiscriminator(book));
    var sj = new StringJoiner(":");
    // build the longname by collecting the ancestor account names in reverse
    for (int i = parents.size() - 1; i >= 0; i--) {
      if (parents.get(i).getTreeLeft() > 1) {
        sj.add(parents.get(i).getName());
      }
    }
    // append the parent/current node
    sj.add(parent.getName());
    sj.add(account.getName());
    var longname = sj.toString();
    logger.debug(longname);

    return longname;
  }

  public Optional<Account> getPrevSibling(Account account, Book book) {

    return this.nodeRepository.getPrevSibling(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public Optional<Account> getNextSibling(Account account, Book book) {

    return this.nodeRepository.getNextSibling(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public Iterable<Account> getParents(Account account, Book book) {

    return this.nodeRepository.getParents(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void insertAsFirstRoot(Account account, Book book) {

    isRootNodeInsertable(account);
    this.nodeRepository.insertAsFirstRoot(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void insertAsLastRoot(Account account, Book book) {

    isRootNodeInsertable(account);
    this.nodeRepository.insertAsLastRoot(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void insertAsLastChildOf(Account account, Account parent, Book book) {

    account.setLongName(this.deriveLongName(account, book));
    this.nodeRepository.insertAsLastChildOf(
        account, parent, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void insertAsFirstChildOf(Account account, Account parent, Book book) {

    account.setLongName(this.deriveLongName(account, book));
    this.nodeRepository.insertAsFirstChildOf(
        account, parent, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void insertAsPrevSiblingOf(Account account, Account sibling, Book book) {

    if (this.findById(account.getParentId()).isPresent()) {
      account.setLongName(this.deriveLongName(account, book));
      this.nodeRepository.insertAsPrevSiblingOf(
          account, sibling, AccountTreeDiscriminatorFactory.getDiscriminator(book));
    } else {
      throw new AccountNotFoundException(
          account.getParentId(),
          "Attempt to identify the parent account of an account to be inserted as a previous sibling.");
    }
  }

  public void insertAsNextSiblingOf(Account account, Account sibling, Book book) {

    if (this.findById(account.getParentId()).isPresent()) {
      account.setLongName(this.deriveLongName(account, book));
      this.nodeRepository.insertAsNextSiblingOf(
          account, sibling, AccountTreeDiscriminatorFactory.getDiscriminator(book));
    } else {
      throw new AccountNotFoundException(
          account.getParentId(),
          "Attempt to identify the parent account of an account to be inserted as a next sibling.");
    }
  }

  public void removeSingle(Account account, Book book) {

    this.nodeRepository.removeSingle(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  public void removeSubTree(Account account, Book book) {

    this.nodeRepository.removeSubtree(
        account, AccountTreeDiscriminatorFactory.getDiscriminator(book));
  }

  /**
   * Prevent some changes from being persisted to the database
   *
   * <ul>
   *   <li>1) root level nodes may only be created/updated by specific repository methods
   * </ul>
   */
  private void preventUnsafeChanges(Account account) {

    if (Boolean.TRUE.equals(account.isRootNode())) {
      throw new UnsupportedOperationException(
          "A root level account may only be created with specific repository methods.");
    }
  }

  /**
   * Count of root level nodes in the account hierarchy
   *
   * @return count of root level nodes in the account hierarchy
   */
  public Long rootLevelNodeCount(Account account) {

    try {
      var cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Long> cq = cb.createQuery(Long.class);
      Root<Account> root = cq.from(Account.class);
      cq.select(cb.count(root));
      cq.where(cb.isNull(root.get("parentId")), cb.equal(root.get("book"), account.getBook()));
      return entityManager.createQuery(cq).getSingleResult();
    } catch (NoResultException ex) {
      return 0L;
    }
  }

  /**
   * Determines if an account may be inserted into the account hierarchy as a root node.
   *
   * <p>In the context of a nested set structure, a root node may only be inserted if
   *
   * <ul>
   *   <li>1) it meets the definition of root node (isRootNode())
   *   <li>2) there isn't already a root node in our hierarchy
   * </ul>
   */
  private void isRootNodeInsertable(Account account) {
    var rootLevelNodeCount = rootLevelNodeCount(account);
    if (rootLevelNodeCount != 0L) {
      throw new AccountCreateException(
          "A new root level account can't be created. Only one root level account may be present. Current root level node count: "
              + rootLevelNodeCount);
    }
  }
}
