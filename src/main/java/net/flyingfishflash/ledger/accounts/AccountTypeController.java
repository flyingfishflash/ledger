package net.flyingfishflash.ledger.accounts;

import java.util.List;
import javax.validation.Valid;
import net.flyingfishflash.ledger.utilities.validators.Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("ledger/api/v1/account-types")
public class AccountTypeController {

  private static final Logger logger = LoggerFactory.getLogger(AccountTypeController.class);

  @Autowired private AccountTypeService accountTypeService;

  // Obtain the List of Account Types
  @GetMapping
  public List<AccountType> findAllAccountTypes() {
    return accountTypeService.findAllAccountTypes();
  }

  // Obtain the List of Account Types associated with an Account Category
  @GetMapping(value = "by-category")
  public List<AccountType> findAccountTypesByCategory(
      @RequestParam(name = "category")
          @Enum(enumClass = net.flyingfishflash.ledger.accounts.AccountCategory.class)
          String category) {
    return accountTypeService.findAccountTypesByCategory(category);
  }
}
