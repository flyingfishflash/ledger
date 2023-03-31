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
package net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNodeInfo;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.Tree;

@SuppressWarnings("java:S119")
public interface NestedNodeRetriever<ID extends Serializable, N extends NestedNode<ID>> {

  List<N> getTreeAsList(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  List<N> getChildren(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Optional<N> getParent(N nodes, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Optional<N> getPrevSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Optional<N> getNextSibling(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Tree<ID, N> getTree(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  List<N> getParents(N node, JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Optional<NestedNodeInfo<ID>> getNodeInfo(ID nodeIds);

  Optional<N> findFirstRoot(JpaTreeDiscriminator<ID, N> suppliedDiscriminator);

  Optional<N> findLastRoot(JpaTreeDiscriminator<ID, N> suppliedDiscriminator);
}
