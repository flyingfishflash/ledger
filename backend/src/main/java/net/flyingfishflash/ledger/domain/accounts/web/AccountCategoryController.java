package net.flyingfishflash.ledger.domain.accounts.web;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import net.flyingfishflash.ledger.core.response.structure.Response;
import net.flyingfishflash.ledger.core.validators.Enum;
import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;
import net.flyingfishflash.ledger.domain.accounts.service.AccountCategoryService;

/** Account Category Controller */
@Tag(name = "account category controller")
@RestController
@Validated
@RequestMapping("/account-categories")
public class AccountCategoryController {

  @Autowired AccountCategoryService accountCategoryService;

  /**
   * Retrieve list of all account categories
   *
   * @return List of all Account Categories
   */
  @GetMapping
  @Operation(summary = "Retrieve list of all account categories")
  public Response<List<AccountCategory>> findAllAccountCategories(HttpServletRequest request) {
    return new Response<>(
        accountCategoryService.findAllAccountCategories(),
        "Retrieve list of all account categories",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }

  /**
   * Retrieve the account category associated with an account type
   *
   * @param type Account type
   * @return Account category associated with supplied account type
   */
  @GetMapping(value = "by-type")
  @Operation(summary = "Retrieve the account category associated with an account type")
  @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Bad Request")})
  public Response<AccountCategory> findAccountCategoriesByType(
      HttpServletRequest request,
      @RequestParam(name = "type") @Enum(enumClass = AccountType.class) String type) {
    return new Response<>(
        accountCategoryService.findAccountCategoryByType(type),
        "Retrieve the account category associated with an account type",
        request.getMethod(),
        URI.create(request.getRequestURI()));
  }
}
