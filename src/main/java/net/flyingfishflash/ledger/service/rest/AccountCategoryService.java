package net.flyingfishflash.ledger.service.rest;

import java.util.List;
import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;
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
