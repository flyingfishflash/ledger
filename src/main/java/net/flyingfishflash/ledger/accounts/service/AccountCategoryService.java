package net.flyingfishflash.ledger.accounts.service;

import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.MapAccountTypeToAccountCategory;
import org.springframework.stereotype.Service;

@Service
public class AccountCategoryService {

  private final static MapAccountTypeToAccountCategory mapAccountTypeToAccountCategory = new MapAccountTypeToAccountCategory();

  public List<AccountCategory> findAllAccountCategories() {
    return mapAccountTypeToAccountCategory.getCategories();
  }

  public AccountCategory findAccountCategoryByType(String type) {
    return mapAccountTypeToAccountCategory.getCategoryByType(type);
  }
}
