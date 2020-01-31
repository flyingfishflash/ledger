package net.flyingfishflash.ledger.accounts.ui;

import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.MapAccountTypeToAccountCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

/*
 * 	Form for creating a new account
 *	GET/POST http://localhost:8080/ledger/accounts/create?parentAccountId=1
 *
 */

@ApiIgnore
@Controller
@RequestMapping("/ledger/accounts/create")
public class AccountCreateController {

  private static final Logger logger = LoggerFactory.getLogger(AccountCreateController.class);

  @Autowired private AccountService accountService;

  @GetMapping // (value = "", method = RequestMethod.GET)
  public String createAccount(
      @RequestParam(name = "parentAccountId") Long parentAccountId, Model model)
      throws Exception {
    logger.debug("@RequestMapping: /ledger/accounts/create (GET)");
    logger.debug("RequestParam: " + parentAccountId);
    if (parentAccountId == null) { parentAccountId = accountService.findRoot().getId(); }
    // TODO Handle NullPointer Exception if parentId does not exist in nested set
    Account parent = accountService.findOneById(parentAccountId);
    Account account = accountService.newAccountNode(parent);
    Boolean parentIsRoot = (parent.getAccountCategory().equals(AccountCategory.Root));
    model.addAttribute("title", "Create Account");
    model.addAttribute("parent", parent);
    model.addAttribute("parentIsRoot", parentIsRoot);
    model.addAttribute("types", MapAccountTypeToAccountCategory.getTypesByCategory(account.getAccountCategory().toString()));
    model.addAttribute("account", account);
    if (parentIsRoot == true) {
      model.addAttribute("categories", MapAccountTypeToAccountCategory.getCategories());
    }
    logger.debug(model.toString());
    return "ledger/accounts/create";
  }

  @PostMapping // (value = "", method = RequestMethod.POST)
  public String saveCreatedAccount(
      Account account,
      @RequestParam("parentAccountId") Long parentAccountId,
      BindingResult result,
      Model model)
      throws Exception {
    logger.debug("@RequestMapping: /ledger/accounts/create (POST)");
    Account parent = accountService.findOneById(parentAccountId);
    account.setParentId(parentAccountId);
    logger.debug(model.toString());
    String method = "last";
    if (method.equals("first")) {
      accountService.insertAsFirstChildOf(account, parent);
    } else {
      accountService.insertAsLastChildOf(account, parent);
    }
    return "redirect:/ledger/accounts";
  }
}
