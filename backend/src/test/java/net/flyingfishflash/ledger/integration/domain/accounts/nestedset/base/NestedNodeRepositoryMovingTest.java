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
public abstract class NestedNodeRepositoryMovingTest extends FunctionalNestedjTest {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Long discriminator) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("discriminator", () -> discriminator);
    return new MapJpaTreeDiscriminator<>(vp);
  }

  @Test
  public void testInsertAsLastChildOfDeepMove() {
    TestNode b = this.findNode("b");
    TestNode a = this.findNode("a");
    this.repository.insertAsLastChildOf(b, a, getDiscriminator(1L));
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");

    refresh(b);
    refresh(a);
    assertEquals(2, (long) c.getTreeLeft());
    assertEquals(9, (long) c.getTreeRight());
    assertEquals(10, (long) b.getTreeLeft());
    assertEquals(15, (long) b.getTreeRight());
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertEquals(11, (long) d.getTreeLeft());
    assertEquals(12, (long) d.getTreeRight());
    assertEquals(1, (long) b.getTreeLevel());
    assertEquals(2, (long) d.getTreeLevel());
    assertEquals(this.getParent(b), a);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildOfDeepMove() {
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    this.repository.insertAsFirstChildOf(c, a, getDiscriminator(1L));
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode b = this.findNode("b");

    refresh(c);
    refresh(a);
    assertEquals(2, (long) c.getTreeLeft());
    assertEquals(9, (long) c.getTreeRight());
    assertEquals(10, (long) b.getTreeLeft());
    assertEquals(15, (long) b.getTreeRight());
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertEquals(11, (long) d.getTreeLeft());
    assertEquals(12, (long) d.getTreeRight());
    assertEquals(2, (long) g.getTreeLevel());
    assertEquals(1, (long) c.getTreeLevel());
    assertEquals(this.getParent(c), a);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfDeepMove() {
    TestNode b = this.findNode("b");
    TestNode a = this.findNode("a");
    this.repository.insertAsNextSiblingOf(b, a, getDiscriminator(1L));
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode e = this.findNode("e");

    refresh(b);
    refresh(a);
    assertEquals(11, (long) b.getTreeLeft());
    assertEquals(16, (long) b.getTreeRight());
    assertEquals(1, (long) a.getTreeLeft());
    assertEquals(10, (long) a.getTreeRight());
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertEquals(12, (long) d.getTreeLeft());
    assertEquals(13, (long) d.getTreeRight());
    assertEquals(0, (long) b.getTreeLevel());
    assertEquals(1, (long) d.getTreeLevel());
    assertEquals(1, (long) e.getTreeLevel());
    assertNull(this.getParent(b));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsPrevSiblingOfDeepMove() {
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    this.repository.insertAsPrevSiblingOf(c, a, getDiscriminator(1L));
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode f = this.findNode("f");
    TestNode h = this.findNode("h");

    refresh(c);
    refresh(a);
    assertEquals(1, (long) c.getTreeLeft());
    assertEquals(8, (long) c.getTreeRight());
    assertEquals(9, (long) a.getTreeLeft());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(4, (long) g.getTreeLeft());
    assertEquals(7, (long) g.getTreeRight());
    assertEquals(11, (long) d.getTreeLeft());
    assertEquals(12, (long) d.getTreeRight());
    assertEquals(0, (long) c.getTreeLevel());
    assertEquals(1, (long) f.getTreeLevel());
    assertEquals(1, (long) g.getTreeLevel());
    assertEquals(2, (long) h.getTreeLevel());
    assertNull(this.getParent(c));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsPrevSiblingOfMoveRight() {
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    this.repository.insertAsPrevSiblingOf(d, g, getDiscriminator(1L));
    TestNode f = this.findNode("f");
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode e = this.findNode("e");
    TestNode h = this.findNode("h");

    refresh(d);
    refresh(g);
    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(5, (long) b.getTreeRight());
    assertEquals(7, (long) f.getTreeLeft());
    assertEquals(8, (long) f.getTreeRight());
    assertEquals(11, (long) g.getTreeLeft());
    assertEquals(14, (long) g.getTreeRight());
    assertEquals(9, (long) d.getTreeLeft());
    assertEquals(10, (long) d.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) d.getTreeLevel());

    assertEquals(this.getParent(d), c);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsPrevSiblingOfMoveLeft() {
    TestNode g = this.findNode("g");
    TestNode e = this.findNode("e");
    this.repository.insertAsPrevSiblingOf(g, e, getDiscriminator(1L));
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode h = this.findNode("h");

    refresh(g);
    refresh(e);
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertEquals(6, (long) h.getTreeLeft());
    assertEquals(7, (long) h.getTreeRight());
    assertEquals(9, (long) e.getTreeLeft());
    assertEquals(10, (long) e.getTreeRight());
    assertEquals(11, (long) b.getTreeRight());
    assertEquals(12, (long) c.getTreeLeft());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) g.getTreeLevel());
    assertEquals(3, (long) h.getTreeLevel());
    assertEquals(this.getParent(g), b);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfMoveRight() {
    TestNode d = this.findNode("d");
    TestNode f = this.findNode("f");
    this.repository.insertAsNextSiblingOf(d, f, getDiscriminator(1L));
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode e = this.findNode("e");
    TestNode h = this.findNode("h");

    refresh(d);
    refresh(f);
    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(5, (long) b.getTreeRight());
    assertEquals(7, (long) f.getTreeLeft());
    assertEquals(8, (long) f.getTreeRight());
    assertEquals(11, (long) g.getTreeLeft());
    assertEquals(14, (long) g.getTreeRight());
    assertEquals(9, (long) d.getTreeLeft());
    assertEquals(10, (long) d.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) d.getTreeLevel());
    assertEquals(this.getParent(d), c);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfMoveLeft() {
    TestNode g = this.findNode("g");
    TestNode d = this.findNode("d");
    this.repository.insertAsNextSiblingOf(g, d, getDiscriminator(1L));
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode e = this.findNode("e");
    TestNode h = this.findNode("h");

    refresh(d);
    refresh(g);
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertEquals(6, (long) h.getTreeLeft());
    assertEquals(7, (long) h.getTreeRight());
    assertEquals(9, (long) e.getTreeLeft());
    assertEquals(10, (long) e.getTreeRight());
    assertEquals(11, (long) b.getTreeRight());
    assertEquals(12, (long) c.getTreeLeft());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) g.getTreeLevel());
    assertEquals(3, (long) h.getTreeLevel());
    assertEquals(this.getParent(g), b);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsLastChildOfMoveLeft() {
    TestNode g = this.findNode("g");
    TestNode b = this.findNode("b");
    this.repository.insertAsLastChildOf(g, b, getDiscriminator(1L));
    TestNode f = this.findNode("f");
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode d = this.findNode("d");
    TestNode h = this.findNode("h");

    refresh(g);
    refresh(b);
    assertEquals(7, (long) g.getTreeLeft());
    assertEquals(10, (long) g.getTreeRight());
    assertEquals(8, (long) h.getTreeLeft());
    assertEquals(9, (long) h.getTreeRight());
    assertEquals(11, (long) b.getTreeRight());
    assertEquals(13, (long) f.getTreeLeft());
    assertEquals(14, (long) f.getTreeRight());
    assertEquals(12, (long) c.getTreeLeft());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) g.getTreeLevel());
    assertEquals(3, (long) h.getTreeLevel());
    assertEquals(this.getParent(g), b);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsLastChildOfMoveRight() {
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    this.repository.insertAsLastChildOf(d, g, getDiscriminator(1L));
    TestNode f = this.findNode("f");
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode e = this.findNode("e");
    TestNode h = this.findNode("h");

    refresh(d);
    refresh(g);
    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(5, (long) b.getTreeRight());
    assertEquals(7, (long) f.getTreeLeft());
    assertEquals(9, (long) g.getTreeLeft());
    assertEquals(12, (long) d.getTreeLeft());
    assertEquals(13, (long) d.getTreeRight());
    assertEquals(10, (long) h.getTreeLeft());
    assertEquals(11, (long) h.getTreeRight());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(3, (long) d.getTreeLevel());
    assertEquals(this.getParent(d), g);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildOfMoveRight() {
    TestNode d = findNode("d");
    TestNode g = findNode("g");
    this.repository.insertAsFirstChildOf(d, g, getDiscriminator(1L));

    refresh(d);
    refresh(g);
    TestNode f = findNode("f");
    TestNode c = findNode("c");
    TestNode a = findNode("a");
    TestNode b = findNode("b");
    TestNode e = findNode("e");
    TestNode h = findNode("h");

    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(5, (long) b.getTreeRight());
    assertEquals(7, (long) f.getTreeLeft());
    assertEquals(9, (long) g.getTreeLeft());
    assertEquals(10, (long) d.getTreeLeft());
    assertEquals(11, (long) d.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(3, (long) d.getTreeLevel());
    assertEquals(this.getParent(d), g);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildOfMoveLeft() {
    TestNode g = this.findNode("g");
    TestNode b = this.findNode("b");
    this.repository.insertAsFirstChildOf(g, b, getDiscriminator(1L));
    TestNode f = this.findNode("f");
    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    TestNode h = this.findNode("h");

    refresh(g);
    refresh(b);
    assertEquals(3, (long) g.getTreeLeft());
    assertEquals(6, (long) g.getTreeRight());
    assertEquals(13, (long) f.getTreeLeft());
    assertEquals(14, (long) f.getTreeRight());
    assertEquals(12, (long) c.getTreeLeft());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) g.getTreeLevel());
    assertEquals(3, (long) h.getTreeLevel());
    assertEquals(this.getParent(g), b);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfMoveEdge() {
    TestNode h = this.findNode("h");
    TestNode c = this.findNode("c");
    this.repository.insertAsLastChildOf(h, c, getDiscriminator(1L));

    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    c = this.findNode("c");
    TestNode d = this.findNode("d");
    TestNode e = this.findNode("e");
    TestNode g = this.findNode("g");
    TestNode f = this.findNode("f");
    h = this.findNode("h");

    assertEquals(1, (long) a.getTreeLeft());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(2, (long) b.getTreeLeft());
    assertEquals(7, (long) b.getTreeRight());
    assertEquals(8, (long) c.getTreeLeft());
    assertEquals(15, (long) c.getTreeRight());
    assertEquals(3, (long) d.getTreeLeft());
    assertEquals(4, (long) d.getTreeRight());
    assertEquals(5, (long) e.getTreeLeft());
    assertEquals(6, (long) e.getTreeRight());
    assertEquals(9, (long) f.getTreeLeft());
    assertEquals(10, (long) f.getTreeRight());
    assertEquals(11, (long) g.getTreeLeft());
    assertEquals(12, (long) g.getTreeRight());
    assertEquals(13, (long) h.getTreeLeft());
    assertEquals(14, (long) h.getTreeRight());
    assertEquals(this.getParent(h), c);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfMoveSecondRoot() {

    TestNode c = this.findNode("c");
    TestNode a = this.findNode("a");
    this.repository.insertAsNextSiblingOf(c, a, getDiscriminator(1L));

    a = this.findNode("a");
    TestNode b = this.findNode("b");
    c = this.findNode("c");
    TestNode d = this.findNode("d");
    TestNode e = this.findNode("e");
    TestNode g = this.findNode("g");
    TestNode f = this.findNode("f");
    TestNode h = this.findNode("h");

    assertEquals(1, (long) a.getTreeLeft());
    assertEquals(8, (long) a.getTreeRight());
    assertEquals(2, (long) b.getTreeLeft());
    assertEquals(7, (long) b.getTreeRight());
    assertEquals(9, (long) c.getTreeLeft());
    assertEquals(16, (long) c.getTreeRight());
    assertEquals(3, (long) d.getTreeLeft());
    assertEquals(4, (long) d.getTreeRight());
    assertEquals(5, (long) e.getTreeLeft());
    assertEquals(6, (long) e.getTreeRight());
    assertEquals(10, (long) f.getTreeLeft());
    assertEquals(11, (long) f.getTreeRight());
    assertEquals(12, (long) g.getTreeLeft());
    assertEquals(15, (long) g.getTreeRight());
    assertEquals(13, (long) h.getTreeLeft());
    assertEquals(14, (long) h.getTreeRight());
    assertNull(this.getParent(c));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertFirstRootAsFirstRoot() {
    TestNode x = this.createTestNode("x");
    this.repository.insertAsFirstRoot(x, getDiscriminator(1L));
    flushAndClear();
    this.repository.insertAsFirstRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    TestNode a = findNode("a");
    assertEquals(1, (long) x.getTreeLeft());
    assertEquals(2, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());

    assertEquals(3, (long) a.getTreeLeft());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(0, (long) a.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertLastRootAsLastRoot() {
    TestNode x = this.createTestNode("x");
    this.repository.insertAsLastRoot(x, getDiscriminator(1L));
    flushAndClear();
    this.repository.insertAsLastRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    TestNode a = findNode("a");
    assertEquals(17, (long) x.getTreeLeft());
    assertEquals(18, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());

    assertEquals(1, (long) a.getTreeLeft());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(0, (long) a.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertLastRootAsFirstRoot() {
    TestNode x = this.createTestNode("x");
    this.repository.insertAsLastRoot(x, getDiscriminator(1L));
    flushAndClear();
    this.repository.insertAsFirstRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    TestNode a = findNode("a");
    assertEquals(1, (long) x.getTreeLeft());
    assertEquals(2, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());

    assertEquals(3, (long) a.getTreeLeft());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(0, (long) a.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInserFirstRootAsLastRoot() {
    TestNode x = this.createTestNode("x");
    this.repository.insertAsFirstRoot(x, getDiscriminator(1L));
    flushAndClear();
    this.repository.insertAsLastRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    TestNode a = findNode("a");
    assertEquals(17, (long) x.getTreeLeft());
    assertEquals(18, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());

    assertEquals(1, (long) a.getTreeLeft());
    assertEquals(16, (long) a.getTreeRight());
    assertEquals(0, (long) a.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }
}
