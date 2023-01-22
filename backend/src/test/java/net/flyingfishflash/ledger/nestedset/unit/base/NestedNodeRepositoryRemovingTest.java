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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.JpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.config.jpa.discriminator.MapJpaTreeDiscriminator;
import net.flyingfishflash.ledger.accounts.data.nestedset.model.NestedNode;
import net.flyingfishflash.ledger.nestedset.unit.model.TestNode;

@Transactional
public abstract class NestedNodeRepositoryRemovingTest extends FunctionalNestedjTest {

  public static <ID extends Serializable, N extends NestedNode<ID>>
      JpaTreeDiscriminator<ID, N> getDiscriminator(Long discriminator) {
    Map<String, Supplier<Object>> vp = new HashMap<>();
    vp.put("discriminator", () -> discriminator);
    return new MapJpaTreeDiscriminator<>(vp);
  }

  @Test
  public void testRemoveSubtreeWithoutChildren() {

    TestNode d = this.findNode("d");
    this.repository.removeSubtree(d, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    TestNode b = this.findNode("b");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode h = this.findNode("h");
    TestNode f = this.findNode("f");

    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(5, (long) b.getTreeRight());
    assertEquals(10, (long) h.getTreeLeft());
    assertEquals(11, (long) h.getTreeRight());
    assertEquals(14, (long) a.getTreeRight());
    assertEquals(6, (long) c.getTreeLeft());
    assertEquals(13, (long) c.getTreeRight());
    assertEquals(9, (long) g.getTreeLeft());
    assertEquals(12, (long) g.getTreeRight());
    assertSecondTreeIntact();
  }

  @Test
  public void testRemoveSubtree() {

    TestNode b = this.findNode("b");
    this.repository.removeSubtree(b, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode h = this.findNode("h");
    TestNode f = this.findNode("f");

    assertEquals(6, (long) h.getTreeLeft());
    assertEquals(7, (long) h.getTreeRight());
    assertEquals(10, (long) a.getTreeRight());
    assertEquals(2, (long) c.getTreeLeft());
    assertEquals(9, (long) c.getTreeRight());
    assertEquals(5, (long) g.getTreeLeft());
    assertEquals(8, (long) g.getTreeRight());
    assertSecondTreeIntact();
  }

  @Test
  public void testRemoveSingleNodeThatHasChildren() {

    TestNode b = this.findNode("b");
    this.repository.removeSingle(b, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode e = this.findNode("e");
    TestNode d = this.findNode("d");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode h = this.findNode("h");

    assertEquals(2, (long) d.getTreeLeft());
    assertEquals(3, (long) d.getTreeRight());
    assertEquals(4, (long) e.getTreeLeft());
    assertEquals(5, (long) e.getTreeRight());
    assertEquals(10, (long) h.getTreeLeft());
    assertEquals(11, (long) h.getTreeRight());
    assertEquals(14, (long) a.getTreeRight());
    assertEquals(6, (long) c.getTreeLeft());
    assertEquals(13, (long) c.getTreeRight());
    assertEquals(9, (long) g.getTreeLeft());
    assertEquals(12, (long) g.getTreeRight());
    assertEquals(1, (long) d.getTreeLevel());
    assertEquals(1, (long) e.getTreeLevel());
    assertSecondTreeIntact();
  }

  @Test
  public void testRemoveSingleNode() {

    TestNode d = this.findNode("d");
    this.repository.removeSingle(d, getDiscriminator(1L));
    TestNode a = this.findNode("a");
    TestNode g = this.findNode("g");
    TestNode c = this.findNode("c");
    TestNode e = this.findNode("e");

    assertEquals(3, (long) e.getTreeLeft());
    assertEquals(4, (long) e.getTreeRight());
    assertEquals(14, (long) a.getTreeRight());
    assertEquals(6, (long) c.getTreeLeft());
    assertEquals(13, (long) c.getTreeRight());
    assertEquals(9, (long) g.getTreeLeft());
    assertEquals(12, (long) g.getTreeRight());
    assertSecondTreeIntact();
  }
}
