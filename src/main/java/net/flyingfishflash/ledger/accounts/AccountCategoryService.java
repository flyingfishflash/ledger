package net.flyingfishflash.ledger.accounts;

import java.util.List;
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

  public List<AccountCategory> findAccountCategoriesByType(String type) {
    return accountTypeCategory.getCategoriesByType(type);
  }
}
