package net.flyingfishflash.ledger.web;

import javax.validation.Valid;

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

import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;
import net.flyingfishflash.ledger.service.AccountService;

/*
 * 	Form for editing an account
 *	GET/POST http://localhost:8080/ledger/accounts/edit?id=2
 * 
 */

@Controller
@RequestMapping("/ledger/accounts/edit")
public class AccountEditController {

	private static final Logger logger = LoggerFactory.getLogger(AccountEditController.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();
	
   
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    // using method rather than method argument due to
    // problems with the the parent account in the POST method
    @ModelAttribute("account")
    public AccountNode formBackingObject(Long id) {
        if (id != null) {
            logger.debug("-- returning existing AccountNode(): " + id);
            return accountRepository.findOneById(id);
        }
        logger.debug("-- returning new AccountNode()");
        return new AccountNode();
    }

    @GetMapping //(value = "", method = RequestMethod.GET)
    public String editAccount(@RequestParam(name="id") Long id,
    					     	   @ModelAttribute("account") AccountNode account,
    					     	   Model model) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/edit (GET)");
    	logger.debug("RequestParam: " + id);
    	AccountNode parent = account.getParent();
    	Boolean parentIsRoot = (parent.getAccountCategory().equals(AccountCategory.Root));
    	logger.debug("parentIsRoot:" + parentIsRoot);
    	logger.debug("parent.toString():" + parent.toString());
    	logger.debug("node.toString():" + account.toString());
        model.addAttribute("title", "Edit Account");
    	model.addAttribute("parentIsRoot", parentIsRoot);
        model.addAttribute("types", atc.getTypesByCategory(account.getAccountCategory().toString()));
        model.addAttribute("destinationAccounts", accountService.getTreeAsList(accountService.getBaseLevelParent(account)));
        Long newParent = parent.getId();
        model.addAttribute("newParent", newParent);
        if (parentIsRoot == true) {
            model.addAttribute("categories", atc.getCategories());
        }
        logger.debug(model.toString());
        return "/ledger/accounts/edit";
    }    

    
    @PostMapping //(value = "", method = RequestMethod.POST)
    public String saveEditedAccount(@RequestParam(name="id") Long id,
    							 @Valid @ModelAttribute("account") AccountNode account,
    							 BindingResult result,
    							 Model model ) throws Exception {
    	logger.debug(result.toString());
        logger.debug(model.toString());
    	logger.debug("@RequestMapping: /ledger/accounts/edit (POST)");
    	logger.debug("node: " + account.toString());
    	accountRepository.update(account);
    	return "redirect:/ledger/accounts";

    }
    
}
