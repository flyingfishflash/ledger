package net.flyingfishflash.ledger.web;

import java.util.Iterator;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountRepository;
import net.flyingfishflash.ledger.domain.AccountType;
import net.flyingfishflash.ledger.domain.AccountTypeCategory;


@Controller
@RequestMapping("/ledger/accounts")
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();
	
    @Autowired
    private AccountRepository accountRepository;


    // Display table of accounts that includes options for create/update/delete
    // TODO Reorder an account in the hierarchy, potentially by changing its parent account
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listNodes(Model model) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts");
    	Iterable<AccountNode> accounts = accountRepository.findWholeTree();
    	// Remove Root node from list of nodes
    	Iterator<AccountNode> it = accounts.iterator();
		while (it.hasNext()) {
		  AccountNode account = it.next();
		  if (account.isRoot()) {
		    it.remove();
		  }
		}
        model.addAttribute("title", "Accounts");
        model.addAttribute("accounts", accounts);
        return "/ledger/accounts/index";
    }
    
    // Delete account and descendants
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteNode(@RequestParam(name="parentAccountId") Long parentAccountId) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/delete (GET)");
    	logger.debug("RequestParam: " + parentAccountId);
    	AccountNode n = accountRepository.findOneById(parentAccountId);
    	accountRepository.removeSubTree(n);
    	//accountRepository.removeSingle(n);
    	return "redirect:/ledger/accounts";
    }

    // Obtain the List of Account Categories
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategories() throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/categories (GET)");
    	return atc.getCategories();

    }

    // Obtain the List of Account Types associated with an Account Category
    @RequestMapping(value = "/typesbycategory", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountType> getTypesByCategory(@RequestParam(name="category") String category) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/typesbycategory (GET)");
    	logger.debug("RequestParam: " + category);
    	return atc.getTypesByCategory(category);

    }
    
    // Obtain the List of Account Categories associated with an Account Type
    @RequestMapping(value = "/categoriesbytype", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategoriesByType(@RequestParam(name="type") String type) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/categoriesbytype (GET)");
    	logger.debug("RequestParam: " + type);
    	return atc.getCategoriesByType(type);

    }

    // Testing placeholder
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(@ModelAttribute("node") AccountNode node, @RequestParam("parentAccountId") Long parentAccountId/*, @RequestParam("method") String method*/) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/test (GET)");
        return "redirect:/ledger/accounts";

    }

}
