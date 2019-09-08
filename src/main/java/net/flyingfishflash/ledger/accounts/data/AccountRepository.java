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
import net.flyingfishflash.ledger.accounts.exceptions.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.exsio.nestedj.repository.DelegatingNestedNodeRepository;

@Repository
@Transactional
public class AccountRepository {

  private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

  @PersistenceContext protected EntityManager entityManager;

  private DelegatingNestedNodeRepository<Long, Account> nodeRepository;

  public AccountRepository(DelegatingNestedNodeRepository<Long, Account> nodeRepository) {
    this.nodeRepository = nodeRepository;
  }

  public Account newAccount() {
    return new Account();
  }

  public void save(Account account) {
    entityManager.persist(account);
  }

  public void update(Account account) {

    // derive and set the longName on all child accounts of the subject account
    Iterable<Account> t = this.nodeRepository.getTreeAsList(account);
    Iterator<Account> it = t.iterator();
    while (it.hasNext()) {
      Account next = it.next();
      next.setLongName(this.deriveLongName(next));
    }
    entityManager.merge(account);
  }

  public Optional<Account> findOneById(Long id) {

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

  public Optional<Account> findOneByGuid(String guid) {

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
        this.findOneById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify parent account of an account for which we are deriving the long name."));

    if (parent.getTreeLeft() <= 1) {
      return account.getName();
    }

    List<Account> parents = (List<Account>) nodeRepository.getParents(parent);
    StringJoiner sj = new StringJoiner(":");
    parents.size();
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

    return nodeRepository.getParents(account);
  }

  public void insertAsLastChildOf(Account account, Account parent) {

    account.setLongName(this.deriveLongName(account));
    nodeRepository.insertAsLastChildOf(account, parent);
  }

  public void insertAsFirstChildOf(Account account, Account parent) {

    account.setLongName(this.deriveLongName(account));
    nodeRepository.insertAsFirstChildOf(account, parent);
  }

  public void insertAsPrevSiblingOf(Account account, Account sibling) {

    Account parent =
        this.findOneById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify the parent account of an account to be inserted as a previous sibling."));
    account.setLongName(this.deriveLongName(account));
    nodeRepository.insertAsPrevSiblingOf(account, sibling);
  }

  public void insertAsNextSiblingOf(Account account, Account sibling) {

    Account parent =
        this.findOneById(account.getParentId())
            .orElseThrow(
                () ->
                    new AccountNotFoundException(
                        account.getParentId(),
                        "Attempt to identify the parent account of an account to be inserted as a next sibling."));
    account.setLongName(this.deriveLongName(account));
    nodeRepository.insertAsNextSiblingOf(account, sibling);
  }

  public void removeSingle(Account account) {

    nodeRepository.removeSingle(account);
  }

  public void removeSubTree(Account account) {

    nodeRepository.removeSubtree(account);
  }

  protected void printNode(Long id, Account account) {

    if (account != null) {
      System.out.println(
          String.format(
              "Node %s: %d/%d/%d",
              account.getId(),
              account.getTreeLeft(),
              account.getTreeRight(),
              account.getTreeLevel()));
    }
  }
}
