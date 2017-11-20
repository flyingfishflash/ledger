package net.flyingfishflash.ledger.web;

import java.util.Iterator;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.flyingfishflash.ledger.domain.AccountCategory;
import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountType;
import net.flyingfishflash.ledger.service.AccountService;


@Controller
@RequestMapping("/ledger/accounts")
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
 	
    
    @Autowired
    private AccountService accountService;


    // Display table of accounts that includes options for create/update/delete
    // TODO Reorder an account in the hierarchy, potentially by changing its parent account
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listNodes(Model model) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts");
    	Iterable<AccountNode> accounts = accountService.findWholeTree();
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
    	AccountNode n = accountService.findOneById(parentAccountId);
    	accountService.removeSubTree(n);
    	//accountRepository.removeSingle(n);
    	return "redirect:/ledger/accounts";
    }

    // Obtain the List of Account Categories
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategories() throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/categories (GET)");
    	return accountService.getCategories();

    }

    // Obtain the List of Account Types associated with an Account Category
    @RequestMapping(value = "/typesbycategory", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountType> getTypesByCategory(@RequestParam(name="category") String category) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/typesbycategory (GET)");
    	logger.debug("RequestParam: " + category);
    	return accountService.getTypesByCategory(category);

    }
    
    // Obtain the List of Account Categories associated with an Account Type
    @RequestMapping(value = "/categoriesbytype", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategoriesByType(@RequestParam(name="type") String type) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/categoriesbytype (GET)");
    	logger.debug("RequestParam: " + type);
    	return accountService.getCategoriesByType(type);

    }

    // Change the position of an account in the hierarchy within the sibling level (down)
    @RequestMapping(value = "/insertAsNextSibling", method = RequestMethod.GET)
    public String insertAsNextSiblingOf(@RequestParam("id") Long id) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/insertAsNextSibling (GET)");
    	AccountNode n = accountService.findOneById(id);
    	if (accountService.getNextSibling(n).isPresent() ){
    		AccountNode s = accountService.getNextSibling(n).get();
    		accountService.insertAsNextSiblingOf(n, s);
    	} else
    	{
    		logger.debug("No Next Sibling");
    	}
        return "redirect:/ledger/accounts";

    }

    // Change the position of an account in the hierarchy within the sibling level (up)
    @RequestMapping(value = "/insertAsPrevSibling", method = RequestMethod.GET)
    public String insertAsPrevSiblingOf(@RequestParam("id") Long id) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/insertAsPrevSibling (GET)");
    	AccountNode n = accountService.findOneById(id);
    	if (accountService.getPrevSibling(n).isPresent() ){
    		AccountNode s = accountService.getPrevSibling(n).get();
    		accountService.insertAsPrevSiblingOf(n, s);
    	} else
    	{
    		logger.debug("No Previous Sibling");
    	}
        return "redirect:/ledger/accounts";

    }

    
    // Testing placeholder
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    //@ResponseBody
    public String test(@RequestParam("id") Long id/*, @RequestParam("method") String method*/) throws Exception {
    	logger.debug("@RequestMapping: /ledger/accounts/test (GET)");
        return "redirect:/ledger/accounts";

    }

}
