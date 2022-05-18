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
package net.flyingfishflash.ledger.accounts.data.nestedset.delegate.control;

import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.LEFT;
import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.RIGHT;

import java.io.Serializable;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeRemover;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.NestedNodeRemovingQueryDelegate;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class QueryBasedNestedNodeRemover<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeRemover<ID, N> {

  private final NestedNodeRemovingQueryDelegate<ID, N> queryDelegate;

  public QueryBasedNestedNodeRemover(NestedNodeRemovingQueryDelegate<ID, N> queryDelegate) {
    this.queryDelegate = queryDelegate;
  }

  @Override
  public void removeSingle(
      NestedNodeInfo<ID> nodeInfo, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    Long from = nodeInfo.right();
    queryDelegate.setNewParentForDeletedNodesChildren(nodeInfo, suppliedDiscriminator);
    queryDelegate.decrementSideFieldsBeforeSingleNodeRemoval(from, RIGHT, suppliedDiscriminator);
    queryDelegate.decrementSideFieldsBeforeSingleNodeRemoval(from, LEFT, suppliedDiscriminator);
    queryDelegate.pushUpDeletedNodesChildren(nodeInfo, suppliedDiscriminator);
    queryDelegate.performSingleDeletion(nodeInfo, suppliedDiscriminator);
  }

  @Override
  public void removeSubtree(
      NestedNodeInfo<ID> nodeInfo, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    Long delta = nodeInfo.right() - nodeInfo.left() + 1;
    Long from = nodeInfo.right();
    queryDelegate.performBatchDeletion(nodeInfo, suppliedDiscriminator);
    queryDelegate.decrementSideFieldsAfterSubtreeRemoval(from, delta, RIGHT, suppliedDiscriminator);
    queryDelegate.decrementSideFieldsAfterSubtreeRemoval(from, delta, LEFT, suppliedDiscriminator);
  }
}
