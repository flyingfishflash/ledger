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
package net.flyingfishflash.ledger.nestedset.unit.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.ex.InvalidNodesHierarchyException;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.nestedset.unit.model.TestNode;

@Transactional
public abstract class NestedNodeRepositoryInsertingTest extends FunctionalNestedjTest {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Long discriminator) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("discriminator", () -> discriminator);
    return new MapJpaTreeDiscriminator<>(vp);
  }

  @Test
  public void testInsertParentToChildAsSibling() {
    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsNextSiblingOf(a, e, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertParentToChildAsChild() {
    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsLastChildOf(a, e, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingSameNode() {
    TestNode a = this.findNode("a");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsNextSiblingOf(a, a, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsLastChildSameNode() {
    TestNode b = this.findNode("b");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsLastChildOf(b, b, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsPrevSiblingSameNode() {
    TestNode c = this.findNode("c");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsPrevSiblingOf(c, c, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildSameNode() {
    TestNode d = this.findNode("d");
    assertThrows(
        InvalidNodesHierarchyException.class,
        () -> this.repository.insertAsFirstChildOf(d, d, getDiscriminator(1L)));
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildOfInsert() {

    TestNode i = this.createTestNode("i");
    TestNode e = this.findNode("e");
    this.repository.insertAsFirstChildOf(i, e, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode h = this.findNode("h");

    assertEquals(6, (long) i.getTreeLeft());
    assertEquals(7, (long) i.getTreeRight());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(9, (long) b.getTreeRight());
    assertEquals(14, (long) h.getTreeLeft());
    assertEquals(15, (long) h.getTreeRight());
    assertEquals((long) i.getTreeLevel(), e.getTreeLevel() + 1);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstChildOfInsertNextToSibling() {

    TestNode i = this.createTestNode("i");
    TestNode b = this.findNode("b");
    this.repository.insertAsFirstChildOf(i, b, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    flush();
    refresh(i);
    refresh(b);
    printNode("i", i);
    b = findNode("b");
    TestNode h = this.findNode("h");
    TestNode d = this.findNode("d");
    TestNode e = this.findNode("e");

    assertEquals(3, (long) i.getTreeLeft());
    assertEquals(4, (long) i.getTreeRight());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(2, (long) b.getTreeLeft());
    assertEquals(9, (long) b.getTreeRight());
    assertEquals(5, (long) d.getTreeLeft());
    assertEquals(6, (long) d.getTreeRight());
    assertEquals(7, (long) e.getTreeLeft());
    assertEquals(8, (long) e.getTreeRight());
    assertEquals(14, (long) h.getTreeLeft());
    assertEquals(15, (long) h.getTreeRight());
    assertEquals((long) i.getTreeLevel(), b.getTreeLevel() + 1);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsLastChildOfInsert() {

    TestNode j = this.createTestNode("j");
    TestNode b = this.findNode("b");
    this.repository.insertAsLastChildOf(j, b, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode h = this.findNode("h");
    TestNode c = this.findNode("c");

    assertEquals(7, (long) j.getTreeLeft());
    assertEquals(8, (long) j.getTreeRight());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(14, (long) h.getTreeLeft());
    assertEquals(15, (long) h.getTreeRight());
    assertEquals(10, (long) c.getTreeLeft());
    assertEquals((long) j.getTreeLevel(), b.getTreeLevel() + 1);
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsPrevSiblingOfInsert() {

    TestNode k = this.createTestNode("k");
    TestNode e = this.findNode("e");
    this.repository.insertAsPrevSiblingOf(k, e, getDiscriminator(1L));
    flushAndClear();
    TestNode a = this.findNode("a");
    TestNode h = this.findNode("h");
    TestNode c = this.findNode("c");

    assertEquals(5, (long) k.getTreeLeft());
    assertEquals(6, (long) k.getTreeRight());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(14, (long) h.getTreeLeft());
    assertEquals(15, (long) h.getTreeRight());
    assertEquals(10, (long) c.getTreeLeft());
    assertEquals(k.getTreeLevel(), e.getTreeLevel());
    assertEquals(k.getParentId(), e.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsNextSiblingOfInsert() {

    TestNode m = this.createTestNode("m");
    TestNode h = this.findNode("h");
    this.repository.insertAsNextSiblingOf(m, h, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");

    assertEquals(14, (long) m.getTreeLeft());
    assertEquals(15, (long) m.getTreeRight());
    assertEquals(18, (long) a.getTreeRight());
    assertEquals(16, (long) g.getTreeRight());
    assertEquals(17, (long) c.getTreeRight());
    assertEquals(m.getTreeLevel(), h.getTreeLevel());
    assertEquals(m.getParentId(), h.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstNodeFirstRoot() {
    this.removeTree();
    TestNode x = this.createTestNode("x");

    this.repository.insertAsFirstRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    assertEquals(1, (long) x.getTreeLeft());
    assertEquals(2, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstNodeLastRoot() {
    this.removeTree();
    TestNode x = this.createTestNode("x");

    this.repository.insertAsLastRoot(x, getDiscriminator(1L));
    flushAndClear();
    x = findNode("x");
    assertEquals(1, (long) x.getTreeLeft());
    assertEquals(2, (long) x.getTreeRight());
    assertEquals(0, (long) x.getTreeLevel());
    assertNull(x.getParentId());
    assertSecondTreeIntact();
  }

  @Test
  public void testInsertAsFirstRoot() {
    TestNode x = this.createTestNode("x");
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
  public void testInsertAsLastRoot() {
    TestNode x = this.createTestNode("x");
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
