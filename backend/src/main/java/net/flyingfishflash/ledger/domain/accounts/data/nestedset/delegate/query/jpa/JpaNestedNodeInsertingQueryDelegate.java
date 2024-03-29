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

import java.io.Serializable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.query.NestedNodeInsertingQueryDelegate;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;

@SuppressWarnings("java:S119")
public class JpaNestedNodeInsertingQueryDelegate<ID extends Serializable, N extends NestedNode<ID>>
    extends JpaNestedNodeQueryDelegate<ID, N> implements NestedNodeInsertingQueryDelegate<ID, N> {

  public JpaNestedNodeInsertingQueryDelegate(
      JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    super(configuration);
  }

  @Override
  public void insert(N node) {
    entityManager.persist(node);
  }

  @Override
  public void incrementSideFieldsGreaterThan(
      Long from, String fieldName, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    updateFields(from, fieldName, false, suppliedDiscriminator);
  }

  @Override
  public void incermentSideFieldsGreaterThanOrEqualTo(
      Long from, String fieldName, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    updateFields(from, fieldName, true, suppliedDiscriminator);
  }

  private void updateFields(
      Long from, String fieldName, boolean gte, JpaTreeDiscriminator<ID, N> suppliedDiscriminator) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaUpdate<N> update = cb.createCriteriaUpdate(nodeClass);
    Root<N> root = update.from(nodeClass);
    update.set(root.<Long>get(fieldName), cb.sum(root.get(fieldName), INCREMENT_BY));
    if (gte) {
      update.where(
          getPredicates(
              cb, root, suppliedDiscriminator, cb.greaterThanOrEqualTo(root.get(fieldName), from)));
    } else {
      update.where(
          getPredicates(
              cb, root, suppliedDiscriminator, cb.greaterThan(root.get(fieldName), from)));
    }
    entityManager.createQuery(update).executeUpdate();
  }
}
