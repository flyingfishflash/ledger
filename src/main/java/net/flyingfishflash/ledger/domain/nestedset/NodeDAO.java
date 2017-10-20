package net.flyingfishflash.ledger.domain.nestedset;

import net.flyingfishflash.ledger.domain.nestedset.Node;
import java.util.List;
import javax.persistence.criteria.CriteriaQuery;

public interface NodeDAO {

    public Node createNode();

    public void save(Node n);

    public void update(Node n);

    public Node findRoot();

    public List<Node> getChildren(Node n);

    public Integer countChildren(Node n);

    public CriteriaQuery<Node> childrenOf(Node n);
    
    public Node getFirstChild(Node n);

    public Node getLastChild(Node n);

    public List<Node> getDescendants(Node n);

    public Integer countDescendants(Node n);

    public List<Node> getSiblings(Node n);

    public CriteriaQuery<Node> siblingsOf(Node n);
    
    public List<Node> descendantsOf(Node n);

    public CriteriaQuery<Node> ancestorsOf(Node n);

    public List<Node> getAncestors(Node n);

    public Node findOneById(Integer nodeId);

    public List<Node> filterByDepth(Integer depth);

    public Node getParent(Node n);

    public Boolean hasPrevSibling(Node n);

    public Node getPrevSibling(Node n);

    public Boolean hasNextSibling(Node n);

    public Node getNextSibling(Node n);

    public Integer deleteDescendants(Node n);

    public List<Node> findWholeTree();

    public List<Node> findAllLeafNodes();
    
    public List<Node> findLeafNodes(Node n);
    
    public void updateChildrenCollection(Node n, boolean children, String direction);

    public void deleteNode(Node n);
    
    public void removeSubtree(Node n);
    
    //public void moveToOtherTree(Node node, Node newParent); // Broken
}
