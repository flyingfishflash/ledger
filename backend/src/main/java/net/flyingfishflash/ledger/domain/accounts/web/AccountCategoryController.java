package net.flyingfishflash.ledger.domain.accounts.web;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.core.validators.Enum;
import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.service.AccountCategoryService;

@Tag(name = "account category controller")
@RestController
@Validated
@RequestMapping("/account-categories")
public class AccountCategoryController {

  @Autowired AccountCategoryService accountCategoryService;

  // Obtain the List of Account Categories
  @GetMapping
  @Operation(summary = "Retrieve all account categories")
  public List<AccountCategory> findAllAccountCategories() {
    return accountCategoryService.findAllAccountCategories();
  }

  // Obtain the List of Account Categories associated with an Account Type
  @GetMapping(value = "by-type")
  @Operation(summary = "Retrieve the account category associated with an account type")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public AccountCategory findAccountCategoriesByType(
      @RequestParam(name = "type") @Enum(enumClass = AccountType.class) String type) {
    return accountCategoryService.findAccountCategoryByType(type);
  }
}
