package net.flyingfishflash.ledger.web;

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

import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;

/*
 * 	Form for creating a new account
 *	GET/POST http://localhost:8080/ledger/accounts/create?parentAccountId=1
 * 
 */

@Controller
@RequestMapping("/ledger/accounts/create")
public class AccountCreateController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountCreateController.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();
	
    
    @Autowired
    private AccountRepository accountRepository;
    
    
    @GetMapping//(value = "", method = RequestMethod.GET)
    public String createAccount(@RequestParam(name="parentAccountId", defaultValue="1") Long parentAccountId, Model model) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/create (GET)");
    	logger.debug("RequestParam: " + parentAccountId);
    	// TODO Handle NullPointer Exception if parentId does not exist in nested set
    	AccountNode parent = accountRepository.findOneById(parentAccountId);
    	AccountNode account = accountRepository.newAccountNode(parent);
    	Boolean parentIsRoot = (parent.getAccountCategory().equals(AccountCategory.Root));
        model.addAttribute("title", "Create Account");
        model.addAttribute("parent", parent);
    	model.addAttribute("parentIsRoot", parentIsRoot);
        model.addAttribute("types", atc.getTypesByCategory(account.getAccountCategory().toString()));
        model.addAttribute("account", account);
        if (parentIsRoot == true) {
            model.addAttribute("categories", atc.getCategories());
        }
        logger.debug(model.toString());
        return "/ledger/accounts/create";
    }
    
    @PostMapping //(value = "", method = RequestMethod.POST)
    public String saveCreatedAccount(AccountNode account
    					   , @RequestParam("parentAccountId") Long parentAccountId
    					   , BindingResult result
    					   , Model model) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/create (POST)");
    	AccountNode parent = accountRepository.findOneById(parentAccountId);
        logger.debug(model.toString());
        String method = "last";
        if (method.equals("first")) {
            accountRepository.insertAsFirstChildOf(account, parent);
        } else {
            accountRepository.insertAsLastChildOf(account, parent);
        }
        return "redirect:/ledger/accounts";

    }
    
}