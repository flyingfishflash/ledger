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
import java.util.Optional;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.NestedNodeRemovingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.ex.InvalidNodeException;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class JpaNestedNodeRemovingQueryDelegate<ID extends Serializable, N extends NestedNode<ID>>
    extends JpaNestedNodeQueryDelegate<ID, N> implements NestedNodeRemovingQueryDelegate<ID, N> {

  // private static final Long UPDATE_INCREMENT_BY = 2L;

  public JpaNestedNodeRemovingQueryDelegate(
      JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    super(configuration);
  }

  @Override
  public void setNewParentForDeletedNodesChildren(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update
        .set(root.get(PARENT_ID), findNodeParentId(node, suppliedDiscriminator).orElse(null))
        .where(
            getPredicates(
                cb,
                root,
                suppliedDiscriminator,
                cb.greaterThanOrEqualTo(root.get(LEFT), node.left()),
                cb.lessThanOrEqualTo(root.get(RIGHT), node.right()),
                cb.equal(root.<Long>get(LEVEL), node.level() + 1)));
    entityManager.createQuery(update).executeUpdate();
  }

  @Override
  public void performSingleDeletion(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaDelete<N> delete = cb.createCriteriaDelete(nodeClass);
    Root<N> root = delete.from(nodeClass);
    delete.where(
        getPredicates(cb, root, suppliedDiscriminator, cb.equal(root.<Long>get(ID), node.id())));
    entityManager.createQuery(delete).executeUpdate();
  }

  private Optional<ID> findNodeParentId(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    if (node.level() > 0) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<ID> select = cb.createQuery(idClass);
      Root<N> root = select.from(nodeClass);
      select
          .select(root.get(ID))
          .where(
              getPredicates(
                  cb,
                  root,
                  suppliedDiscriminator,
                  cb.lessThan(root.get(LEFT), node.left()),
                  cb.greaterThan(root.get(RIGHT), node.right()),
                  cb.equal(root.<Long>get(LEVEL), node.level() - 1)));
      try {
        return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
      } catch (NoResultException ex) {
        throw new InvalidNodeException(
            String.format(
                "Couldn't find node's parent, although its level is greater than 0. It seems the tree is malformed: %s",
                node));
      }
    }
    return Optional.empty();
  }

  @Override
  public void decrementSideFieldsBeforeSingleNodeRemoval(
      Long from, String field, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    decrementSideFields(from, DECREMENT_BY, field, suppliedDiscriminator);
  }

  @Override
  public void pushUpDeletedNodesChildren(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update
        .set(root.<Long>get(RIGHT), cb.diff(root.get(RIGHT), 1L))
        .set(root.<Long>get(LEFT), cb.diff(root.get(LEFT), 1L))
        .set(root.<Long>get(LEVEL), cb.diff(root.get(LEVEL), 1L));

    update.where(
        getPredicates(
            cb,
            root,
            suppliedDiscriminator,
            cb.lessThan(root.get(RIGHT), node.right()),
            cb.greaterThan(root.get(LEFT), node.left())));

    entityManager.createQuery(update).executeUpdate();
  }

  @Override
  public void decrementSideFieldsAfterSubtreeRemoval(
      Long from, Long delta, String field, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    decrementSideFields(from, delta, field, suppliedDiscriminator);
  }

  @Override
  public void performBatchDeletion(
      NestedNodeInfo<ID> node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaDelete<N> delete = cb.createCriteriaDelete(nodeClass);
    Root<N> root = delete.from(nodeClass);
    delete.where(
        getPredicates(
            cb,
            root,
            suppliedDiscriminator,
            cb.greaterThanOrEqualTo(root.get(LEFT), node.left()),
            cb.lessThanOrEqualTo(root.get(RIGHT), node.right())));

    entityManager.createQuery(delete).executeUpdate();
  }

  private void decrementSideFields(
      Long from, Long delta, String field, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);

    update
        .set(root.<Long>get(field), cb.diff(root.get(field), delta))
        .where(
            getPredicates(cb, root, suppliedDiscriminator, cb.greaterThan(root.get(field), from)));

    entityManager.createQuery(update).executeUpdate();
  }
}
