package net.flyingfishflash.ledger.domain.nestedset;

import net.flyingfishflash.ledger.domain.nestedset.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
@Component
@Transactional
public class NodeDAOImpl implements NodeDAO {

    private static final Logger logger = LoggerFactory.getLogger(NodeDAOImpl.class);

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Finds all leaves of given node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public List<Node> findAllLeafNodes(Node n) {
    	
    	// JPA
    	Query q = entityManager.createQuery("SELECT z FROM Node z WHERE rgt = lft + 1");
        List<Node> resultList = q.getResultList();
        if (resultList.size() > 0) return resultList;
        logger.info("Leaves found successfully" + n);
        return null;

        /*
        // HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Node WHERE rgt = lft + 1");
        if (query.list().size() > 0) return query.list();
        */

    }

    /**
     * Creates new node in the tree
     *
     * @return Node
     */
    @Override
    public Node createNode() {
        return new Node();
    }

    /**
     * Updates Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void save(Node n) {

    	// JPA
    	logger.info("NodeDAO.save()");
        entityManager.persist(n);

        /*
        // HIBERNATE
        Session session = sessionFactory.getCurrentSession(); // HIBERNATE
        session.persist(n);
        */

    }    	
        

    /**
     * Updates Node (persist)
     *
     * @return Node
     */
    @Override
    @Transactional
    public void update(Node n) {
    	
    	// JPA
    	logger.info("NodeDAO.update() begin");
        entityManager.merge(n);
    	logger.info("NodeDAO.update() end");

        /*
         // HIBERNATE
         Session session = sessionFactory.getCurrentSession();
         session.update(n);
         */
        
        /*
         // May be needed to prevent exceptions
                  
         if (entity.getId() == null) {
         	entityManager.persist(entity);
         } else {
         	if (!entityManager.contains(entity)) {
         	 entityManager.merge(entity);
          }
         }
         */

    }

    
    /**
     * Finds root of the tree
     *
     * @return Node
     * 
     */
    @Override
    public Node findRoot() {

    	// JPA
    	logger.info("NodeDAO.findRoot() begin");
        Query q = entityManager.createQuery("SELECT z from Node z WHERE lft = 1");
        Node result = (Node) q.getSingleResult(); // Unlike uniqueResult() this will throw an exception on a null result
    	logger.info("NodeDAO.findRoot() end");
        return result;

        /*
         // HIBERNATE
         Session session = sessionFactory.getCurrentSession();
         Query query = session.createQuery("FROM Node WHERE lft = 1");
         return (Node) query.uniqueResult();
         */

    }


    /**
     * Finds whole tree in DB Struture
     *
     * @return
	 *
     * TODO n+1 SELECT
     * Changing Fetch to LAZY resolves but unsure of side effects
     * 
     */
    @Override
    public List<Node> findWholeTree() {

    	logger.info("NodeDAO.findWholeTree() begin");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        cq.orderBy(cb.asc(node.get("lft")));
        TypedQuery<Node> q = entityManager.createQuery(cq);
        logger.info("NodeDAO.findWholeTree() end");
    	return q.getResultList();

    	/* HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Node order by lft asc");
        return query.list();*/
    
    }
    
    
    /**
     * Returns all the children of a root
     * SqlQuery =  "FROM Node WHERE lft > :lft and rgt < :rgt and depth = :depth"
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getChildren(Node n) {
    	logger.info("NodeDAO.getChildren() begin");
        
    	//JPA
        logger.info("NodeDAO.getChildren() begin");
   		TypedQuery<Node> q = entityManager.createQuery(childrenOf(n));
   		logger.info("NodeDAO.getChildren() size (JPA) " + q.getResultList().size());
        logger.info("NodeDAO.getChildren() end");
        return q.getResultList();

        /*
    	//JPA
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.conjunction();
        condition = cb.and(condition, cb.gt(node.get(Node_.lft), n.getLft()));
        condition = cb.and(condition, cb.lt(node.get(Node_.rgt), n.getRgt()));
        condition = cb.and(condition, cb.equal(node.get(Node_.depth), n.getDepth() + 1));        
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("getChildren() size (JPA) " + q.getResultList().size());
        logger.info("NodeDAO.getChildren() begin");
        return q.getResultList();
		*/
        /*
        // HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        Criterion condition1 = Restrictions.and(
                Restrictions.gt("lft", n.getLft()),
                Restrictions.lt("rgt", n.getRgt()),
                Restrictions.eq("depth", n.getDepth() + 1));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(condition1);
   		logger.info("getChildren() size (HIBERNATE) " + criteria.list().size());
        return (List<Node>) criteria.list();
		*/
    }


    @Override
    public CriteriaQuery<Node> childrenOf(Node n) {
    	
    	// JPA
    	logger.info("NodeDAO.childrenOf(Node n) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.depth), n.getDepth() + 1);
   		cq.where(condition);
   		cq.orderBy(cb.asc(node.get("lft")));
    	logger.info("NodeDAO.NodeDAO.childrenOf(Node n) end");
   		return cq;
    	
    }

    
    /**
     * Counts the children
     *
     * @return List<Node>
     */
    @Override
    public Integer countChildren(Node n) {
        return this.getChildren(n).size();
    }

    
    /**
     * Gets the first child of the given node
     *
     * @return Node or null
     */
    @Override
    public Node getFirstChild(Node n) {

    	// JPA
    	logger.info("NodeDAO.getFirstChild(Node n) begin");
    	if (n.isLeaf()) {
            return null;
        } else {
        	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Node> cq = cb.createQuery(Node.class);
            Root<Node> node = cq.from(Node.class);
            cq.select(node);
            Predicate condition = cb.conjunction();
            condition = cb.and(condition, cb.equal(node.get(Node_.lft), n.getLft() + 1));
       		cq.where(condition);
       		TypedQuery<Node> q = entityManager.createQuery(cq);
            logger.info("NodeDAO.getFirstChild(Node n) begin");
            return (Node) q.getSingleResult(); 
        }
    	
    	/*
    	// HIBERNATE
    	if (n.isLeaf()) {
            return null;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, n);
            return (Node) criteria.uniqueResult();
        }
        */
    	
    }

    
    /**
     * Gets the last child
     *
     * @return Node or null
     */
    @Override
    public Node getLastChild(Node n) {

    	// JPA
    	logger.info("NodeDAO.getLastChild(Node n) begin");
    	if (n.isLeaf()) {
            return null;
        } else {
        	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Node> cq = cb.createQuery(Node.class);
            Root<Node> node = cq.from(Node.class);
            cq.select(node);
            Predicate condition = cb.conjunction();
            condition = cb.and(condition, cb.equal(node.get(Node_.rgt), n.getRgt() - 1));
       		cq.where(condition);
       		TypedQuery<Node> q = entityManager.createQuery(cq);
            logger.info("NodeDAO.getLastChild(Node n) begin");
            return (Node) q.getSingleResult(); 
        }

    	/*
    	// HIBERNATE
    	if (n.isLeaf()) {
            return null;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, n);
            criteria.addOrder(Order.desc("lft"));
            return (Node) criteria.uniqueResult();
        }
        */
    }
    
    
    /**
     * Gets all descendants
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getDescendants(Node n) {
        if (n.isLeaf()) {
            return new ArrayList<Node>();
        }
        
        return this.descendantsOf(n);
       
        /*
        // HIBERNATE
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        return (List<Node>) this.descendantsOf(criteria, n).list();
        */
    }

    
    /**
     * Counts all descendants
     *
     * @return Integer
     */
    @Override
    public Integer countDescendants(Node n) {
        return this.getDescendants(n).size();
    }

    /**
     * Gets descendants of given Node
     *
     * @param n
     * @return List<Node>
     */
    /*
    @Override
    public Criteria descendantsOf(Criteria c, Node n) {
        Criterion condition = Restrictions.and(
                Restrictions.gt("lft", n.getLft()),
                Restrictions.lt("lft", n.getRgt()));
        return c.add(condition);
    }
    */
    
    //@Override
    public List<Node> descendantsOf(Node n) {
    	logger.info("NodeDAO.descendantsOf(Node n) begin");
        
    	//JPA
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.conjunction();
        condition = cb.and(condition, cb.gt(node.get(Node_.lft), n.getLft()));
        condition = cb.and(condition, cb.lt(node.get(Node_.rgt), n.getRgt()));
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.descendantsOf(Node n) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.descendantsOf(Node n) end");
        return q.getResultList();    	
    }

    
    /**
     * Gets ancestors of given node
     *
     * @param n
     * @return List<Node>
     */
    @Override
    public List<Node> getAncestors(Node n) {
        if (n.isRoot()) {
            return new ArrayList<Node>();
        } else {
        	//JPA
            logger.info("NodeDAO.getAncestors() begin");
       		TypedQuery<Node> q = entityManager.createQuery(ancestorsOf(n));
       		logger.info("NodeDAO.getAncestors() size (JPA) " + q.getResultList().size());
            logger.info("NodeDAO.getAncestors() end");
            return q.getResultList();

        }
    }

    /**
     * Gets ancestors of given node
     *
     * @param n
     * @return CriteriaQuery<Node>
     */
    @Override
    public CriteriaQuery<Node> ancestorsOf(Node n) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.conjunction();
        condition = cb.and(condition, cb.lt(node.get(Node_.lft), n.getLft()));
        condition = cb.and(condition, cb.gt(node.get(Node_.rgt), n.getRgt()));
   		cq.where(condition);
        cq.orderBy(cb.asc(node.get("rgt")));
    	return cq;
    }
    

    /**
     * Finds node by id
     *
     * @param nodeId
     * @return Node
     */
    @Override
    public Node findOneById(Integer nodeId) {
    	
    	logger.info("NodeDAO.findOneById()");
    	/* HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("id", nodeId));
        return (Node) criteria.uniqueResult(); */ 
        
    	// TESTED
    	// TODO Exception thrown when no result provided
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        Predicate condition = cb.equal(node.get(Node_.id), nodeId);
        cq.where(condition);
        cq.select(node);
        TypedQuery<Node> q = entityManager.createQuery(cq);
        return (Node) q.getSingleResult();
    }

    /**
     * Filters node by depth (tree level)
     *
     * @param depth
     * @return List<Node>
     */
    @Override
    public List<Node> filterByDepth(Integer depth) {

    	//JPA
    	logger.info("filterByDepth(Integer depth) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.depth), depth);
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.filterByDepth(Integer depth) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.filterByDepth(Integer depth) end");
        return q.getResultList();    	    	    	
    	
        /*
    	// HIBERNATE
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("depth", depth));
        return (List<Node>) criteria.list();
        */
    }

    /**
     * Gets parent of given node
     *
     * @param n
     * @return Node
     */
    @Override
    public Node getParent(Node n) {
    	
   		// JPA
    	logger.info("NodeDAO.getParent(Node n) begin");
    	TypedQuery<Node> q = entityManager.createQuery(ancestorsOf(n));
    	// the results are sorted by rgt ascending
   		q.setMaxResults(1);
    	logger.info("NodeDAO.getParent(Node n) end");
   		return q.getSingleResult();
   		
    }

    /**
     * Gets siblings
     *
     * @return List<Node>
     */
    @Override
    public List<Node> getSiblings(Node n) {
    	logger.info("NodeDAO.getSiblings() begin");
    	if (n.isRoot()) {
            return null;
        } else {
            
        	//JPA
       		TypedQuery<Node> q = entityManager.createQuery(siblingsOf(n));
       		logger.info("NodeDAO.getSiblings() size (JPA) " + q.getResultList().size());
            logger.info("NodeDAO.getSiblings() end");
            return q.getResultList();        	
        	}
        	
        	/*
        	// HIBERNATE
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Node.class);
            this.childrenOf(criteria, this.getParent(n));
            if (!includeNode) {
                this.prune(criteria, n);
            }
            return (List<Node>) criteria.list();
            */
    }

    
    /**
     * Gets siblings of given Node
     *
     * @param n
     * @return CriteriaQuery<Node>
     */
    @Override
    public CriteriaQuery<Node> siblingsOf(Node n) {
    	logger.info("NodeDAO.siblingsOf(Node n) begin");
        
    	//JPA
    	Node parentNode;
    	if (n.isRoot()) {
    		parentNode = n; }
    	else {
    		parentNode = n.getParent();
    		}
    	
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.conjunction();
        condition = cb.and(condition, cb.equal(node.get(Node_.depth), parentNode.getDepth() + 1));
        condition = cb.and(condition, cb.equal(node.get(Node_.parent), n.getParent().getId()));
   		cq.where(condition);
    	logger.info("NodeDAO.siblingsOf(Node n) end");
   		return cq;
    }
    
    
    /**
     * Return true if node has previous sibling
     *
     * @param n
     * @return boolean
     */
    @Override
    public Boolean hasPrevSibling(Node n) {
        if (n.isValid()) {
            return false;
        }

    	//JPA
    	logger.info("NodeDAO.hasPrevSibling(Node n) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.rgt), n.getRgt() - 1);
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.hasPrevSibling(Node n) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.hasPrevSibling(Node n) end");
   		return q.getResultList().size() > 0;
        
        /* 
        // HIBERNATE
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("rgt", n.getRgt() - 1));
        return criteria.list().size() > 0;
        */
    }

    /**
     * Gets previous sibling for the given node if it exists
     * 
     * When implementing, "if hasPrevSibling then getPrevSibling"
     *
     * @return Node
     */
    @Override
    public Node getPrevSibling(Node n) {
    	//JPA
    	logger.info("NodeDAO.getPrevSibling(Node n) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.rgt), n.getRgt() - 1);
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.getPrevSibling(Node n) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.getPrevSibling(Node n) end");
   		return q.getSingleResult();

    	/*
    	// HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("rgt", n.getLft() - 1));
        criteria.setMaxResults(1);
        return (Node) criteria.uniqueResult();
        */
    }

    
    /**
     * Determines if the node has next sibling
     *
     * @return boolean
     */
    @Override
    public Boolean hasNextSibling(Node n) {
        if (n.isValid()) {
            return false;
        }
    	//JPA
    	logger.info("NodeDAO.hasNextSibling(Node n) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.lft), n.getLft() + 1);
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.hasNextSibling(Node n) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.hasNextSibling(Node n) end");
   		return q.getResultList().size() > 0;

        /*
        // HIBERNATE
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("lft", n.getLft() + 1));
        return criteria.list().size() > 0;
        */
    }

    
    /**
     * Gets next sibling for the given node if it exist
     *
     * When implementing, "if hasNextSibling then getNextSibling"
	 *
     * @return Node
     */
    @Override
    public Node getNextSibling(Node n) {
    	//JPA
    	logger.info("NodeDAO.getNextSibling(Node n) begin");
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        Root<Node> node = cq.from(Node.class);
        cq.select(node);
        Predicate condition = cb.equal(node.get(Node_.lft), n.getLft() + 1);
   		cq.where(condition);
   		TypedQuery<Node> q = entityManager.createQuery(cq);
   		logger.info("NodeDAO.getNextSibling(Node n) size (JPA) " + q.getResultList().size());
    	logger.info("NodeDAO.getNextSibling(Node n) end");
   		return q.getSingleResult();

    	/*
    	// HIBERNATE
    	Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        criteria.add(Restrictions.eq("lft", n.getLft() + 1));
        criteria.setMaxResults(1);
        return (Node) criteria.uniqueResult();
        */
    }


    /**
     * Updates tree collection to be valid tree after inserting new children
     *
     * @param n
     * @param children
     * @param direction
     */
    @Override
    public void updateChildrenCollection(Node n, boolean children, String direction) {

    	logger.info("updateChildrenCollection() begin");
    	// JPA
    	int newLft = n.getLft();
        int newRight = n.getRgt();
        Query q1;
        Query q2;
        if (children && direction.equals("first")) {
            q1 = entityManager.createQuery("update Node set rgt = rgt+2 where rgt > :newRight");
            q2 = entityManager.createQuery("update Node set lft = lft+2 where lft >= :newRight");
            q1.setParameter("newRight", newRight - 1);
            q2.setParameter("newRight", newRight - 1);
        } else if (children && direction.equals("last")) {
            q1 = entityManager.createQuery("update Node set rgt = rgt+2 where rgt > :newLft");
            q2 = entityManager.createQuery("update Node set lft = lft+2 where lft > :newLft");
            q1.setParameter("newLft", newLft - 1);
            q2.setParameter("newLft", newLft - 1);
        } else {
            q1 = entityManager.createQuery("update Node set rgt = rgt+2 where rgt >= :newLft");
            q2 = entityManager.createQuery("update Node set lft = lft+2 where lft > :newLft");
            q1.setParameter("newLft", newLft - 1);
            q2.setParameter("newLft", newLft - 1);
        }
        q1.executeUpdate();
        q2.executeUpdate();
    	
        /*
        //HIBERNATE
    	int newLft = n.getLft();
        int newRight = n.getRgt();
        Session session = sessionFactory.getCurrentSession();
        Query query;
        Query query2;
        if (children && direction.equals("first")) {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt > :newRight");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft >= :newRight");
            query.setParameter("newRight", newRight - 1);
            query2.setParameter("newRight", newRight - 1);
        } else if (children && direction.equals("last")) {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt > :newLft");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft > :newLft");
            query.setParameter("newLft", newLft - 1);
            query2.setParameter("newLft", newLft - 1);
        } else {
            query = session.createSQLQuery("update Node set rgt = rgt+2 where rgt >= :newLft");
            query2 = session.createSQLQuery("update Node set lft = lft+2 where lft > :newLft");
            query.setParameter("newLft", newLft - 1);
            query2.setParameter("newLft", newLft - 1);
        }
        query.executeUpdate();
        query2.executeUpdate();
    	*/
        logger.info("updateChildrenCollection() end");
    }

    /**
     * Deletes node. WARNING: If node has children they would be deleted too.
     * 
     * Relies on the CascadeType.DELETE property
	 * to remove all descendants based on the content
	 * of the parent_id field and the foreign key
	 * relationship the at database level.
	 * 
	 * Setting CascadeType.ALL (specifically PERSIST)
	 * causes the JPA remove operation to fail.
	 * 
	 * I believe this is because there are still descendants
	 * in the children collection. 
	 * 
	 * An alternative would be to use deleteDescendants
	 * method. 
	 * 
	 * Method deleteDescendants should ensure they are
	 * deleted in reverse order.
	 * 
	 * The immediate ancestor (parent) should be deleted
	 * last in a separate transaction.

	 * 
     * @param n
     */
    @Override
    @Transactional
    public void deleteNode(Node n) {
    	
    	logger.info("NodeDAO deleteNode() begin");
        int left = n.getLft();
        int right = n.getRgt();
        int width = right - left + 1;
        entityManager.remove(entityManager.merge(n));
    	Query q1 = entityManager.createQuery("update Node set rgt = rgt - :width where rgt > :rightParam");
    	Query q2 = entityManager.createQuery("update Node set lft = lft - :width where lft >= :rightParam");
        q1.setParameter("rightParam", right);
        q2.setParameter("rightParam", right);
        q1.setParameter("width", width);
        q2.setParameter("width", width);
        q1.executeUpdate();
        q2.executeUpdate();
        entityManager.flush();
    	logger.info("NodeDAO deleteNode() end");
        
        /* HIBERNATE
        Query query;
        Query query2;
        Session session = sessionFactory.getCurrentSession();
        logger.info("begin transaction");
        //session.beginTransaction();
        session.update(n);
        //session.merge(n);
        session.delete(n);
        //session.getTransaction().commit();
        logger.info("commit transaction");
        query = session.createSQLQuery("update Node set rgt = rgt - :width where rgt > :rightParam");
        query2 = session.createSQLQuery("update Node set lft = lft - :width where lft >= :rightParam");
        query.setParameter("rightParam", right);
        query2.setParameter("rightParam", right);
        query.setParameter("width", width);
        query2.setParameter("width", width);
        query.executeUpdate();
        query2.executeUpdate(); */
    }


    /**
     * Deletes all descendants for the given node
     * Instance pooling is wiped out by this command,
     * so existing MenuItem instances are probably invalid (except for the current one)
     *
     * TODO Broken. Cannot delete and merge the entity in the same transaction.
     *
     * @return number of delete nodes
     */
    @Override
    public Integer deleteDescendants(Node n) {
    	logger.info("NodeDAO.deleteDescendants(Node n) begin");
        if (n.isLeaf()) {
            // save one query
            return 0;
        }
        int left = n.getLft();
        List<Node> nodes = this.getDescendants(n);
        int index = 0;
        for (Node node : nodes) {
            entityManager.remove(node);
            index++;
        }
        n.setRgt(left + 1);
        this.update(n);
    	logger.info("NodeDAO.deleteDescendants(Node n) end");
        return index;
        
        /*
        // HIBERNATE
        int left = n.getLft();
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Node.class);
        this.descendantsOf(criteria, n);
        List<Node> nodes = criteria.list();
        int index = 0;
        for (Node node : nodes) {
            session.delete(node);
            index++;
        }
        n.setRgt(left + 1);
        this.update(n);
        return index;
        */
    }

    
    /**
     * Moves node to another subtree/parent (not working yet)
     *
     * @param node
     * @param newParent
     */
    /*
    @Override
    //TODO Convert to JPA
    public void moveToOtherTree(Node node, Node newParent) {
        int width = node.getRgt() - node.getLft() + 1;

        int newpos = newParent.getLft();
        int distance = newpos - node.getLft() + 1;
        int tmpPos = node.getLft();

        if (distance < 0) {
            distance -= width;
            tmpPos += width;
        }

        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE Node SET lft = lft + " + width
                + " where lft >= " + newpos;
        Query query = session.createQuery(hql);
        int _return = query.executeUpdate();

        hql = "UPDATE Node SET rgt = rgt + " + width + " where rgt >= "
                + newpos;
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        // Move subtree into new space
        hql = "UPDATE Node SET lft = lft + " + distance
                + ", rgt = rgt + " + distance + " where lft >= " + tmpPos
                + " and rgt < " + (tmpPos + width);
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        // remove old space
        hql = "UPDATE Node SET lft = lft - " + width + " where lft > "
                + node.getRgt();
        query = session.createQuery(hql);
        _return = query.executeUpdate();

        hql = "UPDATE Node SET rgt = rgt - " + width + " where rgt > "
                + node.getRgt();
        query = session.createQuery(hql);
        _return = query.executeUpdate();


        node.setParent(newParent);
        node.setDepth(newParent.getDepth() + 1);
        this.update(node);

    }
	*/
}
