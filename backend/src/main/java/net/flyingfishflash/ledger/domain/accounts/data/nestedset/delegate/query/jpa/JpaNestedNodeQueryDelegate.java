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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.JpaNestedNodeRepositoryConfiguration;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;

@SuppressWarnings("java:S119")
public abstract class JpaNestedNodeQueryDelegate<
    ID extends Serializable, N extends NestedNode<ID>> {

  private final JpaTreeDiscriminator<ID, N> treeDiscriminator;

  protected final EntityManager entityManager;

  protected final Class<N> nodeClass;

  protected final Class<ID> idClass;

  protected JpaNestedNodeQueryDelegate(JpaNestedNodeRepositoryConfiguration<ID, N> configuration) {
    this.entityManager = configuration.getEntityManager();
    this.treeDiscriminator = configuration.getTreeDiscriminator();
    this.nodeClass = configuration.getNodeClass();
    this.idClass = configuration.getIdClass();
  }

  protected Predicate[] getPredicates(CriteriaBuilder cb, Root<N> root, Predicate... predicates) {
    List<Predicate> predicateList = new ArrayList<>(treeDiscriminator.getPredicates(cb, root));
    Collections.addAll(predicateList, predicates);
    return predicateList.toArray(new Predicate[0]);
  }

  protected Predicate[] getPredicates(
      CriteriaBuilder cb,
      Root<N> root,
      JpaTreeDiscriminator<ID, N> suppliedDiscriminator,
      Predicate... predicates) {
    List<Predicate> predicateList = new ArrayList<>();
    predicateList.addAll(treeDiscriminator.getPredicates(cb, root));
    predicateList.addAll(suppliedDiscriminator.getPredicates(cb, root));
    Collections.addAll(predicateList, predicates);
    return predicateList.toArray(new Predicate[0]);
  }
}
