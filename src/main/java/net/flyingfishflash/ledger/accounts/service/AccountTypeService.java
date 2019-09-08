package net.flyingfishflash.ledger.accounts.service;

import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeService {

  private static final Logger logger = LoggerFactory.getLogger(AccountTypeService.class);
  private static final AccountTypeCategory accountTypeCategory = new AccountTypeCategory();

  public List<AccountType> findAllAccountTypes() {
    return accountTypeCategory.getTypes();
  }

  public List<AccountType> findAccountTypesByCategory(String category) {
    return accountTypeCategory.getTypesByCategory(category);
  }
}
