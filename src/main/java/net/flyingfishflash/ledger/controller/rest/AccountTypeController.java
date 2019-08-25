package net.flyingfishflash.ledger.controller.rest;

import java.util.List;
import net.flyingfishflash.ledger.domain.AccountType;
import net.flyingfishflash.ledger.service.rest.AccountTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ledger/api/v1/account-types")
public class AccountTypeController {

  private static final Logger logger = LoggerFactory.getLogger(AccountTypeController.class);

  @Autowired private AccountTypeService accountTypeService;

  // Obtain the List of Account Types
  @GetMapping
  public List<AccountType> findAllAccountTypes() throws Exception {
    return accountTypeService.findAllAccountTypes();
  }

  // Obtain the List of Account Types associated with an Account Category
  @GetMapping(value = "by-category")
  public List<AccountType> findAccountTypesByCategory(
      @RequestParam(name = "category") String category) throws Exception {
    return accountTypeService.findAccountTypesByCategory(category);
  }
}
