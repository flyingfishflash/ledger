package net.flyingfishflash.ledger.accounts.service;

import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.MapAccountTypeToAccountCategory;
import org.springframework.stereotype.Service;

@Service
public class AccountCategoryService {

  public List<AccountCategory> findAllAccountCategories() {
    return MapAccountTypeToAccountCategory.getCategories();
  }

  public AccountCategory findAccountCategoryByType(String type) {
    return MapAccountTypeToAccountCategory.getCategoryByType(type);
  }
}
