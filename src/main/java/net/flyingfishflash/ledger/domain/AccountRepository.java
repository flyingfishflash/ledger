package net.flyingfishflash.ledger.domain;

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
    	AccountNode node = new AccountNode();
    	node.accountCategory = p.accountCategory;
    	//logger.info(node.accountCategory.toString());
    	return node;
    	
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
    	logger.info("AccountRepository.findRoot() begin");
    	logger.info("AccountRepository.findRoot() *******8");
    	Optional<AccountNode> nodeO = this.ar.getRoot(AccountNode.class);
    	logger.info(nodeO.toString());
    	//AccountNode node;
    	if (nodeO.isPresent()) {
        	logger.info("AccountRepository.findRoot() isPresent end");
    		return nodeO.get();
    	} else {
        	logger.info("AccountRepository.findRoot() end");
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
    	
    	logger.info("AccountRepository.findWholeTree() begin");
    	logger.info("AccountRepository.findWholeTree() end");
    	return this.ar.getTreeAsList(this.findRoot());
    
    }
    

    public String deriveLongName(AccountNode n, AccountNode p) {
    	
    	List<AccountNode> parents = (List<AccountNode>) ar.getParents(p);
    	StringJoiner sj = new StringJoiner(":");
    	parents.size();
		// build the longname by collecting the ancestor account names in reverse
    	for (int i = parents.size()-1; i >= 0; i--) {
			if (parents.get(i).getLeft() > 1)
				sj.add(parents.get(i).getName());
			logger.info("array: " + parents.get(i).getName());
		}
    	// append the parent/current node
    	sj.add(p.getName());
    	sj.add(n.getName());
		String longname = sj.toString();
		logger.info(longname);
		
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

