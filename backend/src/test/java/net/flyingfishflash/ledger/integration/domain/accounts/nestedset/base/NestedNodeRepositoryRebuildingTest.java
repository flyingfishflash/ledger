/*
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.flyingfishflash.ledger.integration.domain.accounts.nestedset.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.integration.domain.accounts.nestedset.model.TestNode;

@Transactional
public abstract class NestedNodeRepositoryRebuildingTest extends FunctionalNestedjTest {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Long discriminator) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("discriminator", () -> discriminator);
    return new MapJpaTreeDiscriminator<>(vp);
  }

  @Test
  public void testInitializeTree() {
    this.removeTree();
    TestNode x = this.createTestNode("x");
    x.setTreeLeft(0L);
    x.setTreeRight(0L);
    x.setTreeLevel(0L);
    save(x);
    flush();

    assertEquals(0L, (long) x.getTreeLeft());
    assertEquals(0L, (long) x.getTreeRight());

    this.repository.rebuildTree(getDiscriminator(1L));
    refresh(x);
    printNode("x", x);
    assertEquals(1, (long) x.getTreeLeft());
    assertEquals(2, (long) x.getTreeRight());
    assertSecondTreeIntact();
  }

  @Test
  public void testDestroyTree() {
    repository.destroyTree(getDiscriminator(1L));
    flushAndClear();

    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    TestNode b = this.findNode("b");
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode h = this.findNode("h");
    TestNode f = this.findNode("f");

    assertEquals(0, (long) a.getTreeLeft());
    assertEquals(0, (long) a.getTreeRight());
    assertEquals(0, (long) b.getTreeLeft());
    assertEquals(0, (long) b.getTreeRight());
    assertEquals(0, (long) c.getTreeLeft());
    assertEquals(0, (long) c.getTreeRight());
    assertEquals(0, (long) d.getTreeLeft());
    assertEquals(0, (long) d.getTreeRight());
    assertEquals(0, (long) e.getTreeLeft());
    assertEquals(0, (long) e.getTreeRight());
    assertEquals(0, (long) f.getTreeLeft());
    assertEquals(0, (long) f.getTreeRight());
    assertEquals(0, (long) g.getTreeLeft());
    assertEquals(0, (long) g.getTreeRight());
    assertEquals(0, (long) h.getTreeLeft());
    assertEquals(0, (long) h.getTreeRight());

    assertNull(this.getParent(a));
    assertEquals(this.getParent(b), a);
    assertEquals(this.getParent(c), a);
    assertEquals(this.getParent(d), b);
    assertEquals(this.getParent(e), b);
    assertEquals(this.getParent(f), c);
    assertEquals(this.getParent(g), c);
    assertEquals(this.getParent(h), g);

    assertEquals(0, (long) e.getTreeLevel());
    assertEquals(0, (long) f.getTreeLevel());
    assertEquals(0, (long) g.getTreeLevel());
    assertEquals(0, (long) b.getTreeLevel());
    assertEquals(0, (long) c.getTreeLevel());
    assertEquals(0, (long) h.getTreeLevel());
    assertEquals(0, (long) a.getTreeLevel());
    assertEquals(0, (long) d.getTreeLevel());

    assertSecondTreeIntact();
  }

  @Test
  public void testRebuildTree() {

    this.breakTree();
    this.resetParent("c");
    this.repository.rebuildTree(getDiscriminator(1L));

    flushAndClear();

    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    TestNode b = this.findNode("b");
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode h = this.findNode("h");
    TestNode f = this.findNode("f");

    assertNull(this.getParent(a));
    assertEquals(this.getParent(b), a);
    assertNull(this.getParent(c));
    assertEquals(this.getParent(d), b);
    assertEquals(this.getParent(e), b);
    assertEquals(this.getParent(f), c);
    assertEquals(this.getParent(g), c);
    assertEquals(this.getParent(h), g);

    assertEquals(2, (long) e.getTreeLevel());
    assertEquals(1, (long) f.getTreeLevel());
    assertEquals(1, (long) g.getTreeLevel());
    assertEquals(1, (long) b.getTreeLevel());
    assertEquals(0, (long) c.getTreeLevel());
    assertEquals(2, (long) h.getTreeLevel());

    assertSecondTreeIntact();
  }

  @Test
  public void testRebuildWithSecondRoot() {

    TestNode i = this.createTestNode("i");
    TestNode j = this.createTestNode("j");
    TestNode k = this.createTestNode("k");
    TestNode a = this.findNode("a");
    this.repository.insertAsNextSiblingOf(i, a, getDiscriminator(1L));
    this.repository.insertAsLastChildOf(j, i, getDiscriminator(1L));
    this.repository.insertAsLastChildOf(k, i, getDiscriminator(1L));

    breakTree();
    flushAndClear();
    this.repository.rebuildTree(getDiscriminator(1L));

    a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode c = this.findNode("c");
    TestNode d = this.findNode("d");
    TestNode e = this.findNode("e");
    TestNode g = this.findNode("g");
    TestNode f = this.findNode("f");
    TestNode h = this.findNode("h");

    i = findNode("i");
    j = findNode("j");
    k = findNode("k");

    printNode("i", i);
    printNode("j", j);
    printNode("k", k);

    assertNull(this.getParent(a));
    assertEquals(this.getParent(b), a);
    assertEquals(this.getParent(c), a);
    assertEquals(this.getParent(d), b);
    assertEquals(this.getParent(e), b);
    assertEquals(this.getParent(f), c);
    assertEquals(this.getParent(g), c);
    assertEquals(this.getParent(h), g);
    assertNull(this.getParent(i));
    assertEquals(this.getParent(j), i);
    assertEquals(this.getParent(k), i);

    assertSecondTreeIntact();
  }
}
