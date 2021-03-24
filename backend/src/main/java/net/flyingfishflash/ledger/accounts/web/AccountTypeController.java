package net.flyingfishflash.ledger.accounts.web;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;
import net.flyingfishflash.ledger.accounts.service.AccountTypeService;
import net.flyingfishflash.ledger.foundation.validators.Enum;

@RestController
@Validated
@RequestMapping("api/v1/ledger/account-types")
public class AccountTypeController {

  private static final Logger logger = LoggerFactory.getLogger(AccountTypeController.class);

  @Autowired private AccountTypeService accountTypeService;

  // Obtain the List of Account Types
  @GetMapping
  @Operation(summary = "Retrieve all account types")
  public List<AccountType> findAllAccountTypes() {
    return accountTypeService.findAllAccountTypes();
  }

  // Obtain the List of Account Types associated with an Account Category
  @GetMapping(value = "by-category")
  @Operation(summary = "Retrieve the account types associated with an account category")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public List<AccountType> findAccountTypesByCategory(
      @RequestParam(name = "category") @Enum(enumClass = AccountCategory.class) String category) {
    return accountTypeService.findAccountTypesByCategory(category);
  }
}
