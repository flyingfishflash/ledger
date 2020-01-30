package net.flyingfishflash.ledger.accounts.service;

import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.data.MapAccountTypeToAccountCategory;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeService {

  public List<AccountType> findAllAccountTypes() {
    return MapAccountTypeToAccountCategory.getTypes();
  }

  public List<AccountType> findAccountTypesByCategory(String category) {
    return MapAccountTypeToAccountCategory.getTypesByCategory(category);
  }
}
