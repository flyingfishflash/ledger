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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.query.NestedNodeRetrievingQueryDelegate;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNodeInfo;

@SuppressWarnings("java:S119")
public class JpaNestedNodeRetrievingQueryDelegate<ID extends Serializable, N extends NestedNode<ID>>
    extends JpaNestedNodeQueryDelegate<ID, N> implements NestedNodeRetrievingQueryDelegate<ID, N> {

  // private static final Long UPDATE_INCREMENT_BY = 2L;

  public JpaNestedNodeRetrievingQueryDelegate(
      JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    super(configuration);
  }

  @Override
  public List<N> getTreeAsList(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb,
                root,
                discriminator,
                cb.greaterThanOrEqualTo(root.get(LEFT), node.getTreeLeft()),
                cb.lessThanOrEqualTo(root.get(RIGHT), node.getTreeRight())))
        .orderBy(cb.asc(root.<Long>get(LEFT)));

    return entityManager.createQuery(select).getResultList();
  }

  @Override
  public List<N> getChildren(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb,
                root,
                discriminator,
                cb.greaterThanOrEqualTo(root.get(LEFT), node.getTreeLeft()),
                cb.lessThanOrEqualTo(root.get(RIGHT), node.getTreeRight()),
                cb.equal(root.<Long>get(LEVEL), node.getTreeLevel() + 1)))
        .orderBy(cb.asc(root.<Long>get(LEFT)));
    return entityManager.createQuery(select).getResultList();
  }

  @Override
  public Optional<N> getParent(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    if (node.getTreeLevel() > 0) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<N> select = cb.createQuery(nodeClass);
      Root<N> root = select.from(nodeClass);
      select
          .where(
              getPredicates(
                  cb,
                  root,
                  discriminator,
                  cb.lessThan(root.<Long>get(LEFT), node.getTreeLeft()),
                  cb.greaterThan(root.<Long>get(RIGHT), node.getTreeRight()),
                  cb.equal(root.<Long>get(LEVEL), node.getTreeLevel() - 1)))
          .orderBy(cb.asc(root.<Long>get(LEFT)));
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<N> getParents(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    if (node.getTreeLevel() > 0) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<N> select = cb.createQuery(nodeClass);
      Root<N> root = select.from(nodeClass);
      select
          .where(
              getPredicates(
                  cb,
                  root,
                  discriminator,
                  cb.lessThan(root.<Long>get(LEFT), node.getTreeLeft()),
                  cb.greaterThan(root.<Long>get(RIGHT), node.getTreeRight())))
          .orderBy(cb.desc(root.<Long>get(LEFT)));
      return entityManager.createQuery(select).getResultList();
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public Optional<N> getPrevSibling(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb,
                root,
                discriminator,
                cb.equal(root.<Long>get(RIGHT), node.getTreeLeft() - 1),
                cb.equal(root.<Long>get(LEVEL), node.getTreeLevel())))
        .orderBy(cb.asc(root.<Long>get(LEFT)));
    try {
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<N> getNextSibling(N node, JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(
            getPredicates(
                cb,
                root,
                discriminator,
                cb.equal(root.<Long>get(LEFT), node.getTreeRight() + 1),
                cb.equal(root.<Long>get(LEVEL), node.getTreeLevel())))
        .orderBy(cb.asc(root.<Long>get(LEFT)));
    try {
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<NestedNodeInfo<ID>> getNodeInfo(ID nodeId) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<NestedNodeInfo> select = cb.createQuery(NestedNodeInfo.class);
    Root<N> root = select.from(nodeClass);
    select
        .select(
            cb.construct(
                NestedNodeInfo.class,
                root.get(ID),
                root.get(PARENT_ID),
                root.get(LEFT),
                root.get(RIGHT),
                root.get(LEVEL)))
        .where(cb.equal(root.get(ID), nodeId));
    try {
      return Optional.of(entityManager.createQuery(select).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<N> findFirstRoot(JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(getPredicates(cb, root, discriminator, cb.equal(root.<Long>get(LEVEL), 0L)))
        .orderBy(cb.asc(root.<Long>get(LEFT)));
    try {
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<N> findLastRoot(JpaTreeDiscriminator<ID, N> discriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<N> select = cb.createQuery(nodeClass);
    Root<N> root = select.from(nodeClass);
    select
        .where(getPredicates(cb, root, discriminator, cb.equal(root.<Long>get(LEVEL), 0L)))
        .orderBy(cb.desc(root.<Long>get(LEFT)));
    try {
      return Optional.of(entityManager.createQuery(select).setMaxResults(1).getSingleResult());
    } catch (NoResultException ex) {
      return Optional.empty();
    }
  }
}
