package net.flyingfishflash.ledger.accounts.data.nestedset;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeHierarchyManipulator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeInserter;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeMover;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeRebuilder;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeRemover;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeRetriever;
import net.flyingfishflash.ledger.accounts.data.nestedset.ex.InvalidNodeException;
import net.flyingfishflash.ledger.accounts.data.nestedset.ex.InvalidParentException;
import net.flyingfishflash.ledger.accounts.data.nestedset.ex.RepositoryLockedException;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.Tree;

/** {@inheritDoc} */
@SuppressWarnings("java:S119")
public class DelegatingNestedNodeRepository<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeRepository<ID, N> {

  private final NestedNodeInserter<ID, N> inserter;

  private final NestedNodeMover<ID, N> mover;

  private final NestedNodeRemover<ID, N> remover;

  private final NestedNodeRetriever<ID, N> retriever;

  private final NestedNodeRebuilder<ID, N> rebuilder;

  private final Lock<ID, N> lock;

  private boolean allowNullableTreeFields = false;

  public DelegatingNestedNodeRepository(
      NestedNodeMover<ID, N> mover,
      NestedNodeRemover<ID, N> remover,
      NestedNodeRetriever<ID, N> retriever,
      NestedNodeRebuilder<ID, N> rebuilder,
      NestedNodeInserter<ID, N> inserter,
      Lock<ID, N> lock) {
    this.inserter = inserter;
    this.mover = mover;
    this.remover = remover;
    this.retriever = retriever;
    this.rebuilder = rebuilder;
    this.lock = lock;
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsFirstChildOf(
      N node, N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () ->
            insertOrMove(
                node,
                parent,
                NestedNodeHierarchyManipulator.Mode.FIRST_CHILD,
                suppliedDiscriminator));
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsLastChildOf(
      N node, N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () ->
            insertOrMove(
                node,
                parent,
                NestedNodeHierarchyManipulator.Mode.LAST_CHILD,
                suppliedDiscriminator));
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsNextSiblingOf(
      N node, N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () ->
            insertOrMove(
                node,
                parent,
                NestedNodeHierarchyManipulator.Mode.NEXT_SIBLING,
                suppliedDiscriminator));
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsPrevSiblingOf(
      N node, N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () ->
            insertOrMove(
                node,
                parent,
                NestedNodeHierarchyManipulator.Mode.PREV_SIBLING,
                suppliedDiscriminator));
  }

  private void insertOrMove(
      N node,
      N parent,
      NestedNodeHierarchyManipulator.Mode mode,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (parent.getId() == null) {
      throw new InvalidParentException("Cannot insert or move to a parent that has null id");
    }
    Optional<net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo<ID>>
        parentInfo = retriever.getNodeInfo(parent.getId());
    if (!parentInfo.isPresent()) {
      throw new InvalidParentException(
          String.format(
              "Cannot insert or move to non existent parent. Parent id: %s", parent.getId()));
    }
    if (node.getId() != null) {
      Optional<NestedNodeInfo<ID>> nodeInfo = retriever.getNodeInfo(node.getId());
      if (nodeInfo.isPresent()) {
        boolean nodeInfoValid = isNodeInfoValid(nodeInfo.get());
        if (nodeInfoValid) {
          this.mover.move(nodeInfo.get(), parentInfo.get(), mode, suppliedDiscriminator);
        } else if (allowNullableTreeFields) {
          this.inserter.insert(node, parentInfo.get(), mode, suppliedDiscriminator);
        } else {
          throw new InvalidNodeException(
              String.format(
                  "Current configuration doesn't allow nullable tree fields: %s", nodeInfo.get()));
        }
      } else {
        this.inserter.insert(node, parentInfo.get(), mode, suppliedDiscriminator);
      }
    } else {
      this.inserter.insert(node, parentInfo.get(), mode, suppliedDiscriminator);
    }
  }

  private boolean isNodeInfoValid(NestedNodeInfo<ID> nodeInfo) {
    return (nodeInfo.left() != null
        && nodeInfo.right() != null
        && nodeInfo.left() > 0
        && nodeInfo.right() > 0);
  }

  /** {@inheritDoc} */
  @Override
  public void removeSingle(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () -> {
          Optional<NestedNodeInfo<ID>> nodeInfo = retriever.getNodeInfo(node.getId());
          if (nodeInfo.isPresent()) {
            this.remover.removeSingle(nodeInfo.get(), suppliedDiscriminator);
          } else {
            throw new InvalidNodeException(
                String.format("Couldn't remove node, was it already removed?: %s", node));
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubtree(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () -> {
          Optional<NestedNodeInfo<ID>> nodeInfo = retriever.getNodeInfo(node.getId());
          if (nodeInfo.isPresent()) {
            this.remover.removeSubtree(nodeInfo.get(), suppliedDiscriminator);
          } else {
            throw new InvalidNodeException(
                String.format("Couldn't remove node subtree, was it already removed?: %s", node));
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  public List<N> getTreeAsList(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getTreeAsList(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public List<N> getChildren(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getChildren(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<N> getParent(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getParent(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<N> getPrevSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getPrevSibling(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<N> getNextSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getNextSibling(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public Tree<ID, N> getTree(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getTree(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public List<N> getParents(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return this.retriever.getParents(node, suppliedDiscriminator);
  }

  /** {@inheritDoc} */
  @Override
  public void rebuildTree(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    // lockRepository(rebuilder::rebuildTree);
    lockRepository(() -> rebuilder.rebuildTree(suppliedDiscriminator));
  }

  /** {@inheritDoc} */
  @Override
  public void destroyTree(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    // lockRepository(rebuilder::destroyTree);
    lockRepository(() -> rebuilder.destroyTree(suppliedDiscriminator));
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsFirstRoot(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () -> {
          Optional<N> firstRoot = retriever.findFirstRoot(suppliedDiscriminator);
          if (firstRoot.isPresent()) {
            if (differentNodes(node, firstRoot.get())) {
              insertOrMove(
                  node,
                  firstRoot.get(),
                  NestedNodeHierarchyManipulator.Mode.PREV_SIBLING,
                  suppliedDiscriminator);
            }
          } else {
            insertAsFirstNode(node);
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  public void insertAsLastRoot(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    lockNode(
        node,
        () -> {
          Optional<N> lastRoot = retriever.findLastRoot(suppliedDiscriminator);
          if (lastRoot.isPresent()) {
            if (differentNodes(node, lastRoot.get())) {
              insertOrMove(
                  node,
                  lastRoot.get(),
                  NestedNodeHierarchyManipulator.Mode.NEXT_SIBLING,
                  suppliedDiscriminator);
            }
          } else {
            insertAsFirstNode(node);
          }
        });
  }

  private boolean differentNodes(N node, N firstRoot) {
    return !firstRoot.getId().equals(node.getId());
  }

  private void insertAsFirstNode(N node) {
    inserter.insertAsFirstNode(node);
  }

  public boolean isAllowNullableTreeFields() {
    return allowNullableTreeFields;
  }

  public void setAllowNullableTreeFields(boolean allowNullableTreeFields) {
    this.allowNullableTreeFields = allowNullableTreeFields;
  }

  private void lockNode(N node, TreeModifier modifier) {
    if (!lock.lockNode(node)) {
      throw new RepositoryLockedException(
          String.format("Nested Node Repository is locked for Node %s. Try again later.", node));
    }
    try {
      modifier.modifyTree();
    } finally {
      lock.unlockNode(node);
    }
  }

  private void lockRepository(TreeModifier modifier) {
    if (!lock.lockRepository()) {
      throw new RepositoryLockedException("Nested Node Repository is locked. Try again later.");
    }
    try {
      modifier.modifyTree();
    } finally {
      lock.unlockRepository();
    }
  }

  private interface TreeModifier {
    void modifyTree();
  }
}
