package net.flyingfishflash.ledger.accounts.service;

import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountCategoryService {

  private static final Logger logger = LoggerFactory.getLogger(AccountCategoryService.class);
  private static final AccountTypeCategory accountTypeCategory = new AccountTypeCategory();

  public List<AccountCategory> findAllAccountCategories() {
    return accountTypeCategory.getCategories();
  }

  public AccountCategory findAccountCategoryByType(String type) {
    return accountTypeCategory.getCategoryByType(type);
  }
}
