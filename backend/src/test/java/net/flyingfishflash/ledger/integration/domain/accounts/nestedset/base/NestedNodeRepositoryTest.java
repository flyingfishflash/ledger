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
public abstract class NestedNodeRepositoryTest extends FunctionalNestedjTest {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Long discriminator) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("discriminator", () -> discriminator);
    return new MapJpaTreeDiscriminator<>(vp);
  }

  @Test
  public void testMultipleOperations() {

    TestNode i = this.createTestNode("i");
    TestNode j = this.createTestNode("j");
    TestNode k = this.createTestNode("k");
    TestNode l = this.createTestNode("l");
    TestNode m = this.createTestNode("m");
    TestNode a = this.findNode("a");

    this.repository.insertAsNextSiblingOf(i, a, getDiscriminator(1L));
    flushAndClear();

    i = findNode("i");
    printNode("i", i);
    this.repository.insertAsLastChildOf(j, i, getDiscriminator(1L));
    flushAndClear();

    i = findNode("i");
    j = findNode("j");
    printNode("i", i);
    printNode("j", j);
    this.repository.insertAsFirstChildOf(k, i, getDiscriminator(1L));
    flushAndClear();

    k = findNode("k");
    this.repository.insertAsNextSiblingOf(l, k, getDiscriminator(1L));
    flushAndClear();
    l = findNode("l");

    this.repository.insertAsPrevSiblingOf(m, l, getDiscriminator(1L));
    flushAndClear();

    System.out.println("ASSERTS");
    a = this.findNode("a");
    TestNode b = this.findNode("b");
    TestNode c = this.findNode("c");
    TestNode d = this.findNode("d");
    TestNode e = this.findNode("e");
    TestNode g = this.findNode("g");
    TestNode f = this.findNode("f");
    TestNode h = this.findNode("h");
    i = this.findNode("i");
    j = this.findNode("j");
    k = this.findNode("k");
    l = this.findNode("l");
    m = this.findNode("m");

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
    assertEquals(14, (long) g.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());

    assertEquals(17, (long) i.getTreeLeft());
    assertEquals(26, (long) i.getTreeRight());
    assertEquals(24, (long) j.getTreeLeft());
    assertEquals(25, (long) j.getTreeRight());
    assertEquals(18, (long) k.getTreeLeft());
    assertEquals(19, (long) k.getTreeRight());
    assertEquals(22, (long) l.getTreeLeft());
    assertEquals(23, (long) l.getTreeRight());
    assertEquals(20, (long) m.getTreeLeft());
    assertEquals(21, (long) m.getTreeRight());

    assertNull(this.getParent(i));
    assertEquals(this.getParent(j), i);
    assertEquals(this.getParent(k), i);
    assertSecondTreeIntact();

    breakTree();
    flushAndClear();

    repository.rebuildTree(getDiscriminator(1L));
    flushAndClear();

    assertSecondTreeIntact();

    System.out.println("ASSERTS AFTER REBUILD");

    a = this.findNode("a");
    b = this.findNode("b");
    c = this.findNode("c");
    d = this.findNode("d");
    e = this.findNode("e");
    g = this.findNode("g");
    f = this.findNode("f");
    h = this.findNode("h");
    i = this.findNode("i");
    j = this.findNode("j");
    k = this.findNode("k");
    l = this.findNode("l");
    m = this.findNode("m");

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
    assertEquals(14, (long) g.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());

    assertEquals(17, (long) i.getTreeLeft());
    assertEquals(26, (long) i.getTreeRight());
    assertEquals(18, (long) j.getTreeLeft());
    assertEquals(19, (long) j.getTreeRight());
    assertEquals(20, (long) k.getTreeLeft());
    assertEquals(21, (long) k.getTreeRight());
    assertEquals(22, (long) l.getTreeLeft());
    assertEquals(23, (long) l.getTreeRight());
    assertEquals(24, (long) m.getTreeLeft());
    assertEquals(25, (long) m.getTreeRight());

    repository.removeSingle(i, getDiscriminator(1L));

    flushAndClear();
    a = this.findNode("a");
    b = this.findNode("b");
    c = this.findNode("c");
    d = this.findNode("d");
    e = this.findNode("e");
    g = this.findNode("g");
    f = this.findNode("f");
    h = this.findNode("h");
    j = this.findNode("j");
    k = this.findNode("k");
    l = this.findNode("l");
    m = this.findNode("m");

    System.out.println("ASSERTS AFTER REMOVE SINGLE");

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
    assertEquals(14, (long) g.getTreeRight());
    assertEquals(12, (long) h.getTreeLeft());
    assertEquals(13, (long) h.getTreeRight());

    assertEquals(17, (long) j.getTreeLeft());
    assertEquals(18, (long) j.getTreeRight());
    assertEquals(0, (long) j.getTreeLevel());
    assertNull(j.getParentId());
    assertEquals(19, (long) k.getTreeLeft());
    assertEquals(20, (long) k.getTreeRight());
    assertEquals(0, (long) k.getTreeLevel());
    assertNull(k.getParentId());
    assertEquals(21, (long) l.getTreeLeft());
    assertEquals(22, (long) l.getTreeRight());
    assertEquals(0, (long) l.getTreeLevel());
    assertNull(l.getParentId());
    assertEquals(23, (long) m.getTreeLeft());
    assertEquals(24, (long) m.getTreeRight());
    assertEquals(0, (long) m.getTreeLevel());
    assertNull(m.getParentId());

    repository.removeSubtree(a, getDiscriminator(1L));
    flushAndClear();

    j = this.findNode("j");
    k = this.findNode("k");
    l = this.findNode("l");
    m = this.findNode("m");

    System.out.println("ASSERTS AFTER REMOVE SUBTREE");

    assertEquals(1, (long) j.getTreeLeft());
    assertEquals(2, (long) j.getTreeRight());
    assertEquals(0, (long) j.getTreeLevel());
    assertNull(j.getParentId());
    assertEquals(3, (long) k.getTreeLeft());
    assertEquals(4, (long) k.getTreeRight());
    assertEquals(0, (long) k.getTreeLevel());
    assertNull(k.getParentId());
    assertEquals(5, (long) l.getTreeLeft());
    assertEquals(6, (long) l.getTreeRight());
    assertEquals(0, (long) l.getTreeLevel());
    assertNull(l.getParentId());
    assertEquals(7, (long) m.getTreeLeft());
    assertEquals(8, (long) m.getTreeRight());
    assertEquals(0, (long) m.getTreeLevel());
    assertNull(m.getParentId());
  }
}
