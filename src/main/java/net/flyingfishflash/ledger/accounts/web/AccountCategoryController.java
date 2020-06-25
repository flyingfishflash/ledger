package net.flyingfishflash.ledger.accounts.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountCategoryService;
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
@RequestMapping("api/v1/ledger/account-categories")
public class AccountCategoryController {

  private static final Logger logger = LoggerFactory.getLogger(AccountCategoryController.class);

  @Autowired
  AccountCategoryService accountCategoryService;

  // Obtain the List of Account Categories
  @GetMapping
  @ApiOperation(value = "Retrieve all account categories")
  public List<AccountCategory> findAllAccountCategories() {
    return accountCategoryService.findAllAccountCategories();
  }

  // Obtain the List of Account Categories associated with an Account Type
  @GetMapping(value = "by-type")
  @ApiOperation(value = "Retrieve the account category associated with an account type")
  @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
  public AccountCategory findAccountCategoriesByType(
      @RequestParam(name = "type")
          @Enum(enumClass = AccountType.class)
          String type) {
    // String z = type.toString();
    return accountCategoryService.findAccountCategoryByType(type);
  }
}
