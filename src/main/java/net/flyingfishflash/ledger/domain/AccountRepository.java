package net.flyingfishflash.ledger.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.exsio.nestedj.repository.DelegatingNestedNodeRepository;

@Component
@Transactional
public class AccountRepository {

  private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);

  @Autowired
  protected DelegatingNestedNodeRepository<Long, AccountNode> nr;

  @PersistenceContext
  protected EntityManager em;

  public AccountNode newAccountNode() {

    AccountNode accountNode = new AccountNode();

    return accountNode;
  }

  public void save(AccountNode n) {
    em.persist(n);
  }

  public void update(AccountNode n) {

    // derive and set the longname on all child accounts of the subject account
    Iterable<AccountNode> t = this.nr.getTreeAsList(n);
    Iterator<AccountNode> it = t.iterator();
    while (it.hasNext()) {
      AccountNode account = it.next();
      account.setLongName(this.deriveLongName(account, this.nr.getParent(account).get()));
    }
    em.merge(n);
  }

  // Note that due to the eager fetch type
  public AccountNode findOneById(Long id) {

    // TODO move to nestedj, DAO layer
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AccountNode> select = cb.createQuery(AccountNode.class);
    Root<AccountNode> root = select.from(AccountNode.class);
    //root.fetch("parent", JoinType.INNER); // to intialize lazy loaded "parent" relation
    select.where(cb.equal(root.get("id"), id));
    AccountNode n = em.createQuery(select).getSingleResult();
    printNode(id, n);
    this.em.refresh(n);
    return n;
  }

  public Iterable<AccountNode> getTreeAsList(AccountNode account) {

    return this.nr.getTreeAsList(account);
  }

  public String deriveLongName(AccountNode n, AccountNode p) {

    if (p.getTreeLeft() <= 1) {
      return n.name;
    }

    List<AccountNode> parents = (List<AccountNode>) nr.getParents(p);
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
    sj.add(p.getName());
    sj.add(n.getName());
    String longname = sj.toString();
    logger.debug(longname);

    return longname;
  }

  public Optional<AccountNode> getPrevSibling(AccountNode n) {

    logger.debug("AccountRepository.getPrevSibling() begin");
    return this.nr.getPrevSibling(n);
  }

  public Optional<AccountNode> getNextSibling(AccountNode n) {

    logger.debug("AccountRepository.getNextSibling() begin");
    return this.nr.getNextSibling(n);
  }

  public Iterable<AccountNode> getParents(AccountNode n) {

    return nr.getParents(n);
  }

  public void insertAsLastChildOf(AccountNode n, AccountNode p) {

    n.setLongName(this.deriveLongName(n, p));
    nr.insertAsLastChildOf(n, p);
  }

  public void insertAsFirstChildOf(AccountNode n, AccountNode p) {

    n.setLongName(this.deriveLongName(n, p));
    nr.insertAsFirstChildOf(n, p);
  }

  public void insertAsPrevSiblingOf(AccountNode n, AccountNode s) {

    nr.insertAsPrevSiblingOf(n, s);
  }

  public void insertAsNextSiblingOf(AccountNode n, AccountNode s) {

    nr.insertAsNextSiblingOf(n, s);
  }

  public void removeSingle(AccountNode n) {

    nr.removeSingle(n);
  }

  public void removeSubTree(AccountNode n) {

    nr.removeSubtree(n);
  }

  protected void printNode(Long id, AccountNode n) {

    if (n != null) {
      System.out.println(
          String.format("Node %s: %d/%d/%d", n.getId(), n.getTreeLeft(), n.getTreeRight(),
              n.getTreeLevel()));
    }
  }
}
