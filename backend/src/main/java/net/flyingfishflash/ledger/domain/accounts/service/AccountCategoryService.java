package net.flyingfishflash.ledger.domain.accounts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;

@Service
public class AccountCategoryService {

  public List<AccountCategory> findAllAccountCategories() {

    List<AccountCategory> categories = new ArrayList<>(Arrays.asList(AccountCategory.values()));
    categories.remove(AccountCategory.ROOT);

    return categories;
  }

  public AccountCategory findAccountCategoryByType(String type) {

    return AccountType.valueOf(type).getAccountCategory();
  }
}
