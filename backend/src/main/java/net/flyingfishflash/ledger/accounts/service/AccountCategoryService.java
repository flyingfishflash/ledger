package net.flyingfishflash.ledger.accounts.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;

@Service
public class AccountCategoryService {

  public List<AccountCategory> findAllAccountCategories() {

    List<AccountCategory> categories = new LinkedList<>(Arrays.asList(AccountCategory.values()));
    categories.remove(AccountCategory.ROOT);

    return categories;
  }

  public AccountCategory findAccountCategoryByType(String type) {

    return AccountType.valueOf(type).getAccountCategory();
  }
}
