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

package net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.jpa;

import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.ID;
import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.LEFT;
import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.LEVEL;
import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.PARENT_ID;
import static net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode.RIGHT;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.NestedNodeRebuildingQueryDelegate;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;

@SuppressWarnings("java:S119")
public class JpaNestedNodeRebuildingQueryDelegate<ID extends Serializable, N extends NestedNode<ID>>
    extends JpaNestedNodeQueryDelegate<ID, N> implements NestedNodeRebuildingQueryDelegate<ID, N> {

  // private static final Long UPDATE_INCREMENT_BY = 2L;

  public JpaNestedNodeRebuildingQueryDelegate(
      JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    super(configuration);
  }

  @Override
  public void destroyTree(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update
        .set(root.<Long>get(LEFT), 0L)
        .set(root.<Long>get(RIGHT), 0L)
        .set(root.<Long>get(LEVEL), 0L)
        .where(getPredicates(cb, root, suppliedDiscriminator));

    entityManager.createQuery(update).executeUpdate();
  }

  @Override
  public N findFirst(JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(getPredicates(cb, root, suppliedDiscriminator, cb.isNull(root.get(PARENT_ID))))
        .orderBy(cb.desc(root.get(ID)));
    return entityManager.createQuery(select).setMaxResults(1).getSingleResult();
  }

  @Override
  public void resetFirst(N first, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update
        .set(root.<Long>get(LEVEL), 0L)
        .set(root.<Long>get(LEFT), 1L)
        .set(root.<Long>get(RIGHT), 2L)
        .where(
            getPredicates(
                cb,
                root,
                suppliedDiscriminator,
                cb.equal(update.getRoot().get(ID), first.getId())));
    entityManager.createQuery(update).executeUpdate();
  }

  @Override
  public List<N> getSiblings(ID first, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb,
                root,
                suppliedDiscriminator,
                cb.isNull(root.get(PARENT_ID)),
                cb.notEqual(root.get(ID), first)))
        .orderBy(cb.asc(root.get(ID)));
    return entityManager.createQuery(select).getResultList();
  }

  @Override
  public List<N> getChildren(N parent, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb, root, suppliedDiscriminator, cb.equal(root.get(PARENT_ID), parent.getId())))
        .orderBy(cb.asc(root.get(ID)));
    return entityManager.createQuery(select).getResultList();
  }
}
