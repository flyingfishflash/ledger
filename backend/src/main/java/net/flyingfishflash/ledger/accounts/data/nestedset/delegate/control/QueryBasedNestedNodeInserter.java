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
import java.util.Optional;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeInserter;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.NestedNodeInsertingQueryDelegate;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class QueryBasedNestedNodeInserter<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeInserter<ID, N> {

  private final NestedNodeInsertingQueryDelegate<ID, N> queryDelegate;

  public QueryBasedNestedNodeInserter(NestedNodeInsertingQueryDelegate<ID, N> queryDelegate) {
    this.queryDelegate = queryDelegate;
  }

  @Override
  public void insert(
      N node,
      NestedNodeInfo<ID> parentInfo,
      Mode mode,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    makeSpaceForNewElement(getMoveFrom(parentInfo, mode), mode, suppliedDiscriminator);
    insertNodeIntoTree(parentInfo, node, mode);
  }

  @Override
  public void insertAsFirstNode(N node) {
    node.setTreeLeft(1L);
    node.setTreeRight(2L);
    node.setTreeLevel(0L);
    node.setParentId(null);
    queryDelegate.insert(node);
  }

  private void insertNodeIntoTree(NestedNodeInfo<ID> parent, N node, Mode mode) {
    Long left = this.getNodeLeft(parent, mode);
    Long right = left + 1;
    Long level = this.getNodeLevel(parent, mode);
    node.setTreeLeft(left);
    node.setTreeRight(right);
    node.setTreeLevel(level);
    node.setParentId(this.getNodeParent(parent, mode).orElse(null));
    queryDelegate.insert(node);
  }

  private void makeSpaceForNewElement(
      Long from, Mode mode, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (applyGte(mode)) {
      queryDelegate.incermentSideFieldsGreaterThanOrEqualTo(from, RIGHT, suppliedDiscriminator);
      queryDelegate.incermentSideFieldsGreaterThanOrEqualTo(from, LEFT, suppliedDiscriminator);
    } else {
      queryDelegate.incrementSideFieldsGreaterThan(from, RIGHT, suppliedDiscriminator);
      queryDelegate.incrementSideFieldsGreaterThan(from, LEFT, suppliedDiscriminator);
    }
  }

  private Long getMoveFrom(NestedNodeInfo<ID> parent, Mode mode) {
    return switch (mode) {
      case PREV_SIBLING, FIRST_CHILD -> parent.left();
      case NEXT_SIBLING, LAST_CHILD -> parent.right();
    };
  }

  private Long getNodeLevel(NestedNodeInfo<ID> parent, Mode mode) {
    return switch (mode) {
      case NEXT_SIBLING, PREV_SIBLING -> parent.level();
      case LAST_CHILD, FIRST_CHILD -> parent.level() + 1;
    };
  }

  private Optional<ID> getNodeParent(NestedNodeInfo<ID> parent, Mode mode) {
    switch (mode) {
      case NEXT_SIBLING, PREV_SIBLING:
        if (parent.parentId() != null) {
          return Optional.of(parent.parentId());
        } else {
          return Optional.empty();
        }
      case LAST_CHILD, FIRST_CHILD:
      default:
        return Optional.of(parent.id());
    }
  }

  private Long getNodeLeft(NestedNodeInfo<ID> parent, Mode mode) {
    return switch (mode) {
      case NEXT_SIBLING -> parent.right() + 1;
      case PREV_SIBLING -> parent.left();
      case FIRST_CHILD -> parent.left() + 1;
      case LAST_CHILD -> parent.right();
    };
  }

  private boolean applyGte(Mode mode) {
    return switch (mode) {
      case NEXT_SIBLING, FIRST_CHILD -> false;
      case PREV_SIBLING, LAST_CHILD -> true;
    };
  }
}
