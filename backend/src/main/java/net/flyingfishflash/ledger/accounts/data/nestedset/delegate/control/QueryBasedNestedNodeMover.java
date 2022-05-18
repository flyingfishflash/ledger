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
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeMover;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.NestedNodeMovingQueryDelegate;
import net.flyingfishflash.ledger.accounts.data.nestedset.ex.InvalidNodesHierarchyException;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class QueryBasedNestedNodeMover<ID extends Serializable, N extends NestedNode<ID>>
    implements NestedNodeMover<ID, N> {

  private static final long DELTA_MULTIPLIER = 2L;

  private enum Sign {
    PLUS,
    MINUS
  }

  private final NestedNodeMovingQueryDelegate<ID, N> queryDelegate;

  public QueryBasedNestedNodeMover(NestedNodeMovingQueryDelegate<ID, N> queryDelegate) {
    this.queryDelegate = queryDelegate;
  }

  @Override
  public void move(
      NestedNodeInfo<ID> nodeInfo,
      NestedNodeInfo<ID> parentInfo,
      Mode mode,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (!canMoveNodeToSelectedParent(nodeInfo, parentInfo)) {
      throw new InvalidNodesHierarchyException(
          "You cannot move a parent node to it's child or move a node to itself");
    }
    Integer nodeCount = queryDelegate.markNodeIds(nodeInfo, suppliedDiscriminator);

    Sign sign = getSign(nodeInfo, parentInfo, mode);
    Long start = getStart(nodeInfo, parentInfo, mode, sign);
    Long stop = getStop(nodeInfo, parentInfo, mode, sign);
    Long delta = getDelta(nodeCount);
    makeSpaceForMovedElement(sign, delta, start, stop, suppliedDiscriminator);

    Long nodeDelta = getNodeDelta(start, stop);
    Sign nodeSign = getNodeSign(sign);
    Long levelModificator = getLevelModificator(nodeInfo, parentInfo, mode);
    performMove(nodeDelta, nodeSign, levelModificator, suppliedDiscriminator);
    updateParent(nodeInfo, parentInfo, mode, suppliedDiscriminator);
  }

  private void updateParent(
      NestedNodeInfo<ID> nodeInfo,
      NestedNodeInfo<ID> parentInfo,
      Mode mode,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    Optional<ID> newParent = getNewParentId(parentInfo, mode);
    if (newParent.isPresent()) {
      queryDelegate.updateParentField(newParent.get(), nodeInfo, suppliedDiscriminator);
    } else {
      queryDelegate.clearParentField(nodeInfo, suppliedDiscriminator);
    }
  }

  private void performMove(
      Long nodeDelta,
      Sign nodeSign,
      Long levelModificator,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (Sign.PLUS.equals(nodeSign)) {
      queryDelegate.performMoveUp(nodeDelta, levelModificator, suppliedDiscriminator);
    } else if (Sign.MINUS.equals(nodeSign)) {
      queryDelegate.performMoveDown(nodeDelta, levelModificator, suppliedDiscriminator);
    }
  }

  private void makeSpaceForMovedElement(
      Sign sign,
      Long delta,
      Long start,
      Long stop,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (Sign.PLUS.equals(sign)) {
      queryDelegate.updateSideFieldsUp(delta, start, stop, RIGHT, suppliedDiscriminator);
      queryDelegate.updateSideFieldsUp(delta, start, stop, LEFT, suppliedDiscriminator);
    } else if (Sign.MINUS.equals(sign)) {
      queryDelegate.updateSideFieldsDown(delta, start, stop, RIGHT, suppliedDiscriminator);
      queryDelegate.updateSideFieldsDown(delta, start, stop, LEFT, suppliedDiscriminator);
    }
  }

  private boolean canMoveNodeToSelectedParent(NestedNodeInfo<ID> node, NestedNodeInfo<ID> parent) {
    return !node.id().equals(parent.id())
        && (node.left() >= parent.left() || node.right() <= parent.right());
  }

  private Optional<ID> getNewParentId(NestedNodeInfo<ID> parent, Mode mode) {
    switch (mode) {
      case NEXT_SIBLING, PREV_SIBLING:
        if (parent.parentId() != null) {
          return Optional.of(parent.parentId());
        } else {
          return Optional.empty();
        }
      case FIRST_CHILD, LAST_CHILD:
      default:
        return Optional.of(parent.id());
    }
  }

  private Long getLevelModificator(NestedNodeInfo<ID> node, NestedNodeInfo<ID> parent, Mode mode) {
    return switch (mode) {
      case NEXT_SIBLING, PREV_SIBLING -> parent.level() - node.level();
      case FIRST_CHILD, LAST_CHILD -> parent.level() + 1 - node.level();
    };
  }

  private Long getNodeDelta(Long start, Long stop) {
    return stop - start - 1;
  }

  private Long getDelta(Integer nodeCount) {
    return nodeCount * DELTA_MULTIPLIER;
  }

  private Sign getNodeSign(Sign sign) {
    return (sign.equals(Sign.PLUS)) ? Sign.MINUS : Sign.PLUS;
  }

  private Sign getSign(NestedNodeInfo<ID> node, NestedNodeInfo<ID> parent, Mode mode) {
    return switch (mode) {
      case PREV_SIBLING, FIRST_CHILD -> (node.right() - parent.left()) > 0 ? Sign.PLUS : Sign.MINUS;
      case NEXT_SIBLING, LAST_CHILD -> (node.left() - parent.right()) > 0 ? Sign.PLUS : Sign.MINUS;
    };
  }

  private Long getStart(NestedNodeInfo<ID> node, NestedNodeInfo<ID> parent, Mode mode, Sign sign) {
    return switch (mode) {
      case PREV_SIBLING -> sign.equals(Sign.PLUS) ? parent.left() - 1 : node.right();
      case FIRST_CHILD -> sign.equals(Sign.PLUS) ? parent.left() : node.right();
      case NEXT_SIBLING -> sign.equals(Sign.PLUS) ? parent.right() : node.right();
      case LAST_CHILD -> sign.equals(Sign.PLUS) ? parent.right() - 1 : node.right();
    };
  }

  private Long getStop(NestedNodeInfo<ID> node, NestedNodeInfo<ID> parent, Mode mode, Sign sign) {
    return switch (mode) {
      case PREV_SIBLING -> sign.equals(Sign.PLUS) ? node.left() : parent.left();
      case FIRST_CHILD -> sign.equals(Sign.PLUS) ? node.left() : parent.left() + 1;
      case NEXT_SIBLING -> sign.equals(Sign.PLUS) ? node.left() : parent.right() + 1;
      case LAST_CHILD -> sign.equals(Sign.PLUS) ? node.left() : parent.right();
    };
  }
}
