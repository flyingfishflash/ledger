package net.flyingfishflash.ledger.integration.domain.accounts.nestedset.base;

import net.flyingfishflash.ledger.integration.domain.accounts.nestedset.model.TestNode;

public interface TestHelper {

  TestNode findNode(String symbol);

  TestNode getParent(TestNode f);

  void breakTree();

  void resetParent(String symbol);

  void removeTree();

  static void printNode(String symbol, TestNode n) {
    if (n != null) {
      System.out.println(
          String.format(
              "Node %s: %d/%d/%d", symbol, n.getTreeLeft(), n.getTreeRight(), n.getTreeLevel()));
    }
  }

  default void flushAndClear() {}

  default void flush() {}

  default void refresh(TestNode node) {}

  void save(TestNode node);
}
