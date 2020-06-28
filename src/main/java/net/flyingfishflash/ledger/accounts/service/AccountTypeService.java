package net.flyingfishflash.ledger.accounts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import org.springframework.stereotype.Service;

@Service
public class AccountTypeService {

  public List<AccountType> findAllAccountTypes() {

    List<AccountType> types = new LinkedList<>(Arrays.asList(AccountType.values()));

    return types;
  }

  public List<AccountType> findAccountTypesByCategory(String category) {

    List<AccountType> accountTypes = new ArrayList<>(AccountType.values().length);

    for (AccountType a : AccountType.values()) {
      if (a.getAccountCategory() == AccountCategory.valueOf(category)) {
        accountTypes.add(a);
      }
    }

    return accountTypes;
  }
}
