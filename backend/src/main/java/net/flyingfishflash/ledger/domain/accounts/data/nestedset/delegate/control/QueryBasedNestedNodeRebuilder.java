/*
 *  The MIT License
 *
 *  Copyright (c) 2019 eXsio.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *  the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 *  BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.control;

import java.io.Serializable;
import java.util.Optional;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeHierarchyManipulator.Mode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeInserter;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeRebuilder;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeRetriever;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.NestedNodeRebuildingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.ex.InvalidNodeException;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class QueryBasedNestedNodeRebuilder<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeRebuilder<ID, N> {

  private final NestedNodeInserter<ID, N> inserter;

  private final NestedNodeRetriever<ID, N> retriever;

  private final NestedNodeRebuildingQueryDelegate<ID, N> queryDelegate;

  public QueryBasedNestedNodeRebuilder(
      NestedNodeInserter<ID, N> inserter,
      NestedNodeRetriever<ID, N> retriever,
      NestedNodeRebuildingQueryDelegate<ID, N> queryDelegate) {
    this.inserter = inserter;
    this.retriever = retriever;
    this.queryDelegate = queryDelegate;
  }

  @Override
  public void rebuildTree(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    N first = queryDelegate.findFirst(suppliedDiscriminator);
    queryDelegate.resetFirst(first, suppliedDiscriminator);
    restoreSiblings(first, suppliedDiscriminator);
    rebuildRecursively(first, suppliedDiscriminator);
    for (N node : queryDelegate.getSiblings(first.getId(), suppliedDiscriminator)) {
      rebuildRecursively(node, suppliedDiscriminator);
    }
  }

  @Override
  public void destroyTree(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    queryDelegate.destroyTree(suppliedDiscriminator);
  }

  private void rebuildRecursively(N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    for (N child : queryDelegate.getChildren(parent, suppliedDiscriminator)) {
      inserter.insert(child, getNodeInfo(parent.getId()), Mode.LAST_CHILD, suppliedDiscriminator);
      rebuildRecursively(child, suppliedDiscriminator);
    }
  }

  private void restoreSiblings(N first, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    for (N node : queryDelegate.getSiblings(first.getId(), suppliedDiscriminator)) {
      inserter.insert(node, getNodeInfo(first.getId()), Mode.NEXT_SIBLING, suppliedDiscriminator);
    }
  }

  private NestedNodeInfo<ID> getNodeInfo(ID nodeId) {
    if (nodeId == null) {
      throw new NullPointerException("nodeId cannot be null");
    }
    Optional<NestedNodeInfo<ID>> nodeInfo = retriever.getNodeInfo(nodeId);
    if (!nodeInfo.isPresent()) {
      throw new InvalidNodeException(String.format("Couldn't find node with Id %s", nodeId));
    }
    return nodeInfo.get();
  }
}
