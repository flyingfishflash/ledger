package net.flyingfishflash.ledger.accounts.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MapAccountTypeToAccountCategory {

  private static final Logger logger = LoggerFactory.getLogger(MapAccountTypeToAccountCategory.class);

  private static final HashMap<AccountType, AccountCategory> map = createMap();

  private static HashMap<AccountType, AccountCategory> createMap() {
    HashMap<AccountType, AccountCategory> map = new HashMap<AccountType, AccountCategory>();
    map.put(AccountType.Asset, AccountCategory.Asset);
    map.put(AccountType.Bank, AccountCategory.Asset);
    map.put(AccountType.Cash, AccountCategory.Asset);
    map.put(AccountType.Mutual, AccountCategory.Asset);
    map.put(AccountType.Stock, AccountCategory.Asset);
    map.put(AccountType.Liability, AccountCategory.Liability);
    map.put(AccountType.Credit, AccountCategory.Liability);
    map.put(AccountType.Income, AccountCategory.Income);
    map.put(AccountType.Expense, AccountCategory.Expense);
    map.put(AccountType.Equity, AccountCategory.Equity);
    map.put(AccountType.Root, AccountCategory.Root);
    return map;
  }

  public Map<AccountType, AccountCategory> get() {

    for (Entry<AccountType, AccountCategory> entry : map.entrySet()) {
      logger.info(entry.toString());
    }

    return map;
  }

  public List<AccountType> getTypes() {
    // natural sort order (as declared in AccountCategory enum)
    final List<AccountType> types =
        new LinkedList<AccountType>(Arrays.asList(AccountType.values()));

    return types;
  }

  public List<AccountCategory> getCategories() {
    // natural sort order (as declared in AccountCategory enum)
    final List<AccountCategory> categories =
        new LinkedList<AccountCategory>(Arrays.asList(AccountCategory.values()));
    categories.remove(AccountCategory.Root);
    // categories.forEach(name -> logger.debug("getCategories: " + name.toString()));

    return categories;
  }

  public List<AccountType> getTypesByCategory(String category) {

    List<AccountType> types =
        map.entrySet().stream()
            .filter(line -> AccountCategory.valueOf(category).equals(line.getValue()))
            // sorted alphabetically
            .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

    // types.forEach(name -> logger.debug("getTypesByCategory: " + name.toString()));

    return types;
  }

  public AccountCategory getCategoryByType(String type) {

    return map.get(AccountType.valueOf(type));
  }
}