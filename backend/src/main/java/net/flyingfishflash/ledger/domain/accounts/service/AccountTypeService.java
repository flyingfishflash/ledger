package net.flyingfishflash.ledger.domain.accounts.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;

@Service
public class AccountTypeService {

  public List<AccountType> findAllAccountTypes() {

    return new ArrayList<>(Arrays.asList(AccountType.values()));
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
