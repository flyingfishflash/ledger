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
import java.util.List;
import java.util.Optional;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeRetriever;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.NestedNodeRetrievingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.InMemoryTree;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNodeInfo;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.Tree;

@SuppressWarnings("java:S119")
public class QueryBasedNestedNodeRetriever<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeRetriever<ID, N> {

  private final NestedNodeRetrievingQueryDelegate<ID, N> queryDelegate;

  public QueryBasedNestedNodeRetriever(NestedNodeRetrievingQueryDelegate<ID, N> queryDelegate) {
    this.queryDelegate = queryDelegate;
  }

  @Override
  public Tree<ID, N> getTree(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    Tree<ID, N> tree = new InMemoryTree<>(node);
    for (N n : queryDelegate.getChildren(node, suppliedDiscriminator)) {
      Tree<ID, N> subtree = this.getTree(n, suppliedDiscriminator);
      tree.addChild(subtree);
    }
    return tree;
  }

  @Override
  public List<N> getTreeAsList(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getTreeAsList(node, suppliedDiscriminator);
  }

  @Override
  public List<N> getChildren(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getChildren(node, suppliedDiscriminator);
  }

  @Override
  public Optional<N> getParent(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getParent(node, suppliedDiscriminator);
  }

  @Override
  public List<N> getParents(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getParents(node, suppliedDiscriminator);
  }

  @Override
  public Optional<N> getPrevSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getPrevSibling(node, suppliedDiscriminator);
  }

  @Override
  public Optional<N> getNextSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.getNextSibling(node, suppliedDiscriminator);
  }

  @Override
  public Optional<NestedNodeInfo<ID>> getNodeInfo(ID nodeId) {
    return queryDelegate.getNodeInfo(nodeId);
  }

  @Override
  public Optional<N> findFirstRoot(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.findFirstRoot(suppliedDiscriminator);
  }

  @Override
  public Optional<N> findLastRoot(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    return queryDelegate.findLastRoot(suppliedDiscriminator);
  }
}
