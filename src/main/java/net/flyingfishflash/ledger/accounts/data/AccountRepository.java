package net.flyingfishflash.ledger.accounts.data;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import net.flyingfishflash.ledger.accounts.exceptions.AccountCreateException;
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.exsio.nestedj.NestedNodeRepository;

@Repository
@Transactional
public class AccountRepository {

  private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

  @PersistenceContext private EntityManager entityManager;

  private final NestedNodeRepository<Long, Account> nodeRepository;

  public AccountRepository(NestedNodeRepository<Long, Account> nodeRepository) {
    this.nodeRepository = nodeRepository;
  }

  public Account newAccount(String guid) {
    return new Account(guid);
  }

  private void save(Account account) {

    preventUnsafeChanges(account);
    entityManager.persist(account);
  }

  public void update(Account account) {

    preventUnsafeChanges(account);

    // derive and set the longName on all child accounts of the subject account
    Iterable<Account> t = this.nodeRepository.getTreeAsList(account);
    Iterator<Account> it = t.iterator();
    while (it.hasNext()) {
      Account next = it.next();
      next.setLongName(this.deriveLongName(next));
    }
    entityManager.merge(account);
  }

  public Optional<Account> findRoot() {

    try {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
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
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
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
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Account> select = cb.createQuery(Account.class);
      Root<Account> root = select.from(Account.class);
      select.where(cb.equal(root.get("guid"), guid));
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  public Iterable<Account> getTreeAsList(Account account) {

    return this.nodeRepository.getTreeAsList(account);
  }

  public String deriveLongName(Account account) {

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

    List<Account> parents = this.nodeRepository.getParents(parent);
    StringJoiner sj = new StringJoiner(":");
    // build the longname by collecting the ancestor account names in reverse
    for (int i = parents.size() - 1; i >= 0; i--) {
      if (parents.get(i).getTreeLeft() > 1) {
        sj.add(parents.get(i).getName());
      }
      // logger.debug("array: " + parents.get(i).getName());
    }
    // append the parent/current node
    sj.add(parent.getName());
    sj.add(account.getName());
    String longname = sj.toString();
    logger.debug(longname);

    return longname;
  }

  public Optional<Account> getPrevSibling(Account account) {

    return this.nodeRepository.getPrevSibling(account);
  }

  public Optional<Account> getNextSibling(Account account) {

    return this.nodeRepository.getNextSibling(account);
  }

  public Iterable<Account> getParents(Account account) {

    return this.nodeRepository.getParents(account);
  }

  public void insertAsFirstRoot(Account account) {

    isRootNodeInsertable(account);
    this.nodeRepository.insertAsFirstRoot(account);
  }

  public void insertAsLastRoot(Account account) {

    isRootNodeInsertable(account);
    this.nodeRepository.insertAsLastRoot(account);
  }

  public void insertAsLastChildOf(Account account, Account parent) {

    account.setLongName(this.deriveLongName(account));
    this.nodeRepository.insertAsLastChildOf(account, parent);
  }

  public void insertAsFirstChildOf(Account account, Account parent) {

    account.setLongName(this.deriveLongName(account));
    this.nodeRepository.insertAsFirstChildOf(account, parent);
  }

  public void insertAsPrevSiblingOf(Account account, Account sibling) {

    Account parent =
        this.findById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify the parent account of an account to be inserted as a previous sibling."));
    account.setLongName(this.deriveLongName(account));
    this.nodeRepository.insertAsPrevSiblingOf(account, sibling);
  }

  public void insertAsNextSiblingOf(Account account, Account sibling) {

    Account parent =
        this.findById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify the parent account of an account to be inserted as a next sibling."));
    account.setLongName(this.deriveLongName(account));
    this.nodeRepository.insertAsNextSiblingOf(account, sibling);
  }

  public void removeSingle(Account account) {

    this.nodeRepository.removeSingle(account);
  }

  public void removeSubTree(Account account) {

    this.nodeRepository.removeSubtree(account);
  }

  /**
   * Prevent some changes from being persisted to the database
   *
   * <ul>
   *   <li>1) root level nodes may only be created/updated by specific repository methods
   * </ul>
   */
  private void preventUnsafeChanges(Account account) {

    if (account.isRootNode()) {
      throw new UnsupportedOperationException(
          "A root level account may only be created with specific repository methods.");
    }
  }

  /** @return count of root level nodes in the account hierarchy */
  public Long rootLevelNodeCount() {

    try {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<Long> cq = cb.createQuery(Long.class);
      Root<Account> root = cq.from(Account.class);
      cq.select(cb.count(root));
      cq.where(cb.isNull(root.get("parentId")));
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
   *
   * @param account - the account to be inserted as a rood node
   * @return true if the account may be inserted in to the hierarchy as a root node, otherwise false
   */
  private void isRootNodeInsertable(Account account) {

    if (rootLevelNodeCount() != 0L) {
      throw new AccountCreateException(
          "A new root level account can't be created. Only one root level account may be present. Current root level node count: "
              + rootLevelNodeCount());
    }
  }
}
