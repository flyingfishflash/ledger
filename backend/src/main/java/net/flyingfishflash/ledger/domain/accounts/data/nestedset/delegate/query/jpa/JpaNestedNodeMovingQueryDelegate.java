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

package net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.jpa;

import static net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode.ID;
import static net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode.LEFT;
import static net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode.LEVEL;
import static net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode.PARENT_ID;
import static net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode.RIGHT;

import java.io.Serializable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.NestedNodeMovingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class JpaNestedNodeMovingQueryDelegate<ID extends Serializable, N extends NestedNode<ID>>
    extends JpaNestedNodeQueryDelegate<ID, N> implements NestedNodeMovingQueryDelegate<ID, N> {

  private enum Mode {
    UP,
    DOWN
  }

  private static final Long MARKING_MODIFIER = 1000L;

  public JpaNestedNodeMovingQueryDelegate(
      JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    super(configuration);
  }

  @Override
  public Integer markNodeIds(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update
        .set(root.<Long>get(RIGHT), markRightField(root))
        .where(
            getPredicates(
                cb,
                root,
                suppliedDiscriminator,
                cb.greaterThanOrEqualTo(root.get(LEFT), node.left()),
                cb.lessThanOrEqualTo(root.get(RIGHT), node.right())));
    return entityManager.createQuery(update).executeUpdate();
  }

  @Override
  public void updateSideFieldsUp(
      Long delta,
      Long start,
      Long stop,
      String field,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    updateFields(Mode.UP, delta, start, stop, field, suppliedDiscriminator);
  }

  @Override
  public void updateSideFieldsDown(
      Long delta,
      Long start,
      Long stop,
      String field,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    updateFields(Mode.DOWN, delta, start, stop, field, suppliedDiscriminator);
  }

  @Override
  public void performMoveUp(
      Long nodeDelta, Long levelModificator, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    performMove(Mode.UP, nodeDelta, levelModificator, suppliedDiscriminator);
  }

  @Override
  public void performMoveDown(
      Long nodeDelta, Long levelModificator, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    performMove(Mode.DOWN, nodeDelta, levelModificator, suppliedDiscriminator);
  }

  @Override
  public void updateParentField(
      ID newParentId, NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (newParentId == null) {
      throw new NullPointerException("newParentId cannot be null");
    }
    doUpdateParentField(newParentId, node, suppliedDiscriminator);
  }

  @Override
  public void clearParentField(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    doUpdateParentField(null, node, suppliedDiscriminator);
  }

  private void updateFields(
      Mode mode,
      Long delta,
      Long start,
      Long stop,
      String field,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);

    if (Mode.DOWN.equals(mode)) {
      update.set(root.<Long>get(field), cb.diff(root.get(field), delta));
    } else if (Mode.UP.equals(mode)) {
      update.set(root.<Long>get(field), cb.sum(root.get(field), delta));
    }
    update.where(
        getPredicates(
            cb,
            root,
            suppliedDiscriminator,
            cb.greaterThan(root.get(field), start),
            cb.lessThan(root.get(field), stop)));
    entityManager.createQuery(update).executeUpdate();
  }

  private void performMove(
      Mode mode,
      Long nodeDelta,
      Long levelModificator,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);

    update.set(root.<Long>get(LEVEL), cb.sum(root.get(LEVEL), levelModificator));
    if (Mode.DOWN.equals(mode)) {
      update.set(root.<Long>get(RIGHT), cb.diff(unMarkRightField(root), nodeDelta));
      update.set(root.<Long>get(LEFT), cb.diff(root.get(LEFT), nodeDelta));
    } else if (Mode.UP.equals(mode)) {
      update.set(root.<Long>get(RIGHT), cb.sum(unMarkRightField(root), nodeDelta));
      update.set(root.<Long>get(LEFT), cb.sum(root.get(LEFT), nodeDelta));
    }
    update.where(getPredicates(cb, root, suppliedDiscriminator, cb.lessThan(root.get(RIGHT), 0)));
    entityManager.createQuery(update).executeUpdate();
  }

  private Expression<Long> markRightField(Root<N> root) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    return cb.diff(cb.neg(root.get(RIGHT)), MARKING_MODIFIER);
  }

  private Expression<Long> unMarkRightField(Root<N> root) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    return cb.neg(cb.sum(root.get(RIGHT), MARKING_MODIFIER));
  }

  private void doUpdateParentField(
      ID newParentId, NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);

    update
        .set(root.get(PARENT_ID), newParentId)
        .where(getPredicates(cb, root, suppliedDiscriminator, cb.equal(root.get(ID), node.id())));

    entityManager.createQuery(update).executeUpdate();
  }
}
