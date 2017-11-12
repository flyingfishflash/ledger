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

import net.flyingfishflash.ledger.common.IdentifierFactory;
import pl.exsio.nestedj.repository.NestedNodeRepositoryImpl;

@Component
@Transactional
public class AccountRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountRepository.class);
	
	@Autowired
	private NestedNodeRepositoryImpl<AccountNode> ar;

    @PersistenceContext
    protected EntityManager em;


    public AccountNode newAccountNode(AccountNode p) {

    	AccountNode accountNode = new AccountNode();
    	
    	accountNode.setGuid(IdentifierFactory.getInstance().generateIdentifier());
    	
    	if (p.accountCategory.equals(AccountCategory.Root)) {
    		accountNode.setAccountCategory(AccountCategory.Asset);
    		accountNode.setAccountType(AccountType.Asset);
    	} else {
    		accountNode.setAccountCategory(p.accountCategory);
    		accountNode.setAccountType(p.accountType);
    	}
    	
    	return accountNode;
  	
    }
    

    public void save(AccountNode n) {
    	em.persist(n);
    }


    public void update(AccountNode n) {

    	// derive and set the longname on all child accounts of the subject account
    	Iterable<AccountNode> t = this.ar.getTreeAsList(n);
        Iterator<AccountNode> it = t.iterator();
		while (it.hasNext()) {
			AccountNode account = it.next();
			account.setLongname(this.deriveLongName(account, account.getParent()));
			}        
        
    	em.merge(n);
    }

    
    public AccountNode findOneById(Long id) {
    	
    	// TODO move to nestedj, DAO layer
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AccountNode> select = cb.createQuery(AccountNode.class);
        Root<AccountNode> root = select.from(AccountNode.class);
        select.where(cb.equal(root.get("id"), id));
        AccountNode n = em.createQuery(select).getSingleResult();
        printNode(id, n);
        this.em.refresh(n);
        return n;

    }
    

    public AccountNode findRoot() {
    	
    	// TODO move to nestedj, DAO layer
    	logger.debug("AccountRepository.findRoot() begin");
    	logger.debug("AccountRepository.findRoot() *******8");
    	Optional<AccountNode> nodeO = this.ar.getRoot(AccountNode.class);
    	logger.debug(nodeO.toString());
    	//AccountNode node;
    	if (nodeO.isPresent()) {
        	logger.debug("AccountRepository.findRoot() isPresent end");
    		return nodeO.get();
    	} else {
        	logger.debug("AccountRepository.findRoot() end");
    		return null;
    	}

    	
        /*
    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AccountNode> cq = cb.createQuery(AccountNode.class);
        Root<AccountNode> node = cq.from(AccountNode.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(AccountNode_.lft), 1);
        cq.where(condition);
        TypedQuery<AccountNode> q = em.createQuery(cq);
        printNode(77L, q.getSingleResult());
        */
        
    	//return q.getSingleResult();    	
    	
    }
    
    
    public Iterable<AccountNode> findWholeTree() {
    	
    	//logger.debug("AccountRepository.findWholeTree() begin");
    	//logger.debug("AccountRepository.findWholeTree() end");
    	return this.ar.getTreeAsList(this.findRoot());
    
    }
    

    public String deriveLongName(AccountNode n, AccountNode p) {
    	
    	if (p.getLeft() <= 1) {
    		return n.name;
    	}
    	
    	List<AccountNode> parents = (List<AccountNode>) ar.getParents(p);
    	StringJoiner sj = new StringJoiner(":");
    	parents.size();
		// build the longname by collecting the ancestor account names in reverse
    	for (int i = parents.size()-1; i >= 0; i--) {
			if (parents.get(i).getLeft() > 1)
				sj.add(parents.get(i).getName());
			//logger.debug("array: " + parents.get(i).getName());
		}
    	// append the parent/current node
    	sj.add(p.getName());
    	sj.add(n.getName());
		String longname = sj.toString();
		logger.debug(longname);
		
		return longname;
		
    }
    
    
    public void insertAsLastChildOf(AccountNode n, AccountNode p) {
    	
    	n.setLongname(this.deriveLongName(n, p));
    	ar.insertAsLastChildOf(n, p);
    
    }
    

    public void insertAsFirstChildOf(AccountNode n, AccountNode p) {
    	
    	n.setLongname(this.deriveLongName(n, p));
    	ar.insertAsFirstChildOf(n, p);
    
    }
    

    public void insertAsPrevSiblingOf(AccountNode n, AccountNode p) {
    	
    	n.setLongname(this.deriveLongName(n, p));
    	ar.insertAsPrevSiblingOf(n, p);
    
    }

    
    public void insertAsNextSiblingOf(AccountNode n, AccountNode p) {
    	
    	n.setLongname(this.deriveLongName(n, p));
    	ar.insertAsNextSiblingOf(n, p);
    
    }

    
    public void removeSingle(AccountNode n) {
    	
    	ar.removeSingle(n);
    	
    }
    
    
    public void removeSubTree(AccountNode n) {
    	
    	ar.removeSubtree(n);
    
    }
    
    
    protected void printNode(Long id, AccountNode n) {
    
    	if(n != null) {
            System.out.println(String.format("Node %s: %d/%d/%d", n.getId(), n.getLeft(), n.getRight(), n.getLevel()));
        }
    
    }

}

