package net.flyingfishflash.ledger.accounts.ui;

import javax.validation.Valid;
import net.flyingfishflash.ledger.accounts.data.Account;
import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountRepository;
import net.flyingfishflash.ledger.accounts.data.MapAccountTypeToAccountCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

/*
 * 	Form for editing an account
 *	GET/POST http://localhost:8080/ledger/accounts/edit?id=2
 *
 */

@ApiIgnore
@Controller
@RequestMapping("/ledger/accounts/edit")
public class AccountEditController {

  private static final Logger logger = LoggerFactory.getLogger(AccountEditController.class);

  @Autowired private AccountRepository accountRepository;

  @Autowired private AccountService accountService;

  // using method rather than method argument due to
  // problems with the the parent account in the POST method
  @ModelAttribute("account")
  public Account formBackingObject(Long id) {
    if (id != null) {
      logger.debug("-- returning existing AccountNode(): " + id);
      return accountRepository.findOneById(id).orElseThrow(() -> new IllegalArgumentException("Account Id " + id + " Not found"));
    }
    logger.debug("-- returning new AccountNode()");
    return new Account();
  }

  @GetMapping // (value = "", method = RequestMethod.GET)
  public String editAccount(
      @RequestParam(name = "id") Long id, @ModelAttribute("account") Account account, Model model)
      throws Exception {
    logger.debug("@RequestMapping: /ledger/accounts/edit (GET)");
    logger.debug("RequestParam: " + id);
    // AccountNode parent = account.getParent();
    Account parent = accountRepository.findOneById(account.getParentId()).orElseThrow(() -> new IllegalArgumentException("Account Id: " + account.getParentId() + " Not found"));
    Boolean parentIsRoot = (parent.getAccountCategory().equals(AccountCategory.Root));
    logger.debug("parentIsRoot:" + parentIsRoot);
    logger.debug("parent.toString():" + parent.toString());
    logger.debug("node.toString():" + account.toString());
    model.addAttribute("title", "Edit Account");
    model.addAttribute("parentIsRoot", parentIsRoot);
    model.addAttribute("types", MapAccountTypeToAccountCategory.getTypesByCategory(account.getAccountCategory().toString()));
    model.addAttribute("destinationAccounts", accountService.getElligibleParentAccounts(account));
    Long newParent = parent.getId();
    logger.debug("after Long newParent = parent.getId();");
    model.addAttribute("newParent", newParent);
    if (parentIsRoot == true) {
      model.addAttribute("categories", MapAccountTypeToAccountCategory.getCategories());
    }
    logger.debug(model.toString());
    return "ledger/accounts/edit";
  }

  @PostMapping // (value = "", method = RequestMethod.POST)
  public String saveEditedAccount(
      @RequestParam(name = "id") Long id,
      @Valid @ModelAttribute("account") Account account,
      @ModelAttribute("newParent") Long newParent,
      BindingResult result,
      Model model)
      throws Exception {
    logger.debug(result.toString());
    logger.debug(model.toString());
    logger.debug("@RequestMapping: /ledger/accounts/edit (POST)");
    logger.debug("node: " + account.toString());
    if (newParent != account.getParentId() && newParent != account.getId()) {
      accountService.insertAsLastChildOf(account, accountRepository.findOneById(newParent).orElseThrow(() -> new IllegalArgumentException("Account Id " + newParent + " Not found")));
    } else {
      accountRepository.update(account);
    }
    return "redirect:/ledger/accounts";
  }
}
