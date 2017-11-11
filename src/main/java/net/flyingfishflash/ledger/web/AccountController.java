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
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private static final AccountTypeCategory atc = new AccountTypeCategory();
	
    @Autowired
    private AccountRepository accountRepository;
   
    
    // Display table of accounts that includes options for create/update/delete
    // TODO Reorder an account in the hierarchy, potentially by changing its parent account
    @RequestMapping(value = "/ledger/accounts", method = RequestMethod.GET)
    public String listNodes(Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts");
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
    
    // Open form for creating a new account
    @RequestMapping(value = "/ledger/accounts/create", method = RequestMethod.GET)
    public String createNode(@RequestParam(name="parentAccountId", defaultValue="1") Long parentAccountId, Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/create (GET)");
    	logger.info("RequestParam: " + parentAccountId);
    	// TODO Handle NullPointer Exception if parentId does not exist in nested set
    	AccountNode parent = accountRepository.findOneById(parentAccountId);
    	AccountNode node = accountRepository.newAccountNode(parent);
    	Boolean parentIsRoot = (parent.getAccountCategory().equals(AccountCategory.Root));
    	logger.info("parent.toString():" + parent.toString());
    	logger.info("node.toString():" + node.toString());
        model.addAttribute("title", "Create Account");
        model.addAttribute("parent", parent);
    	model.addAttribute("parentIsRoot", parentIsRoot);
        model.addAttribute("node", node);
        if (parentIsRoot == true) {
        	logger.debug("Parent AccountCategory is ROOT");
        	model.addAttribute("defaultAccountCategory", AccountCategory.Asset);
            model.addAttribute("types", atc.getTypesByCategory(AccountCategory.Asset.toString()));
            model.addAttribute("categories", atc.getCategories());
        }
        else {
        	logger.debug("Parent AccountCategory is NOT ROOT");
        	model.addAttribute("defaultAccountCategory", parent.getAccountCategory());
            model.addAttribute("types", atc.getTypesByCategory(parent.getAccountCategory().toString()));
        }
        return "/ledger/accounts/create";
    }
    
    // Submit form for creating a new account
    // 	POST http://localhost:8080/ledger/accounts/create?parentAccountId=1
    @RequestMapping(value = "/ledger/accounts/create", method = RequestMethod.POST)
    public String saveNode(@ModelAttribute("node") AccountNode node, @RequestParam("parentAccountId") Long parentAccountId/*, @RequestParam("method") String method*/) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/create (POST)");
    	AccountNode parent = accountRepository.findOneById(parentAccountId);
        logger.info("Parent: " + parent.toString());
        String method = "last";
        if (method.equals("first")) {
            accountRepository.insertAsFirstChildOf(node, parent);
        } else {
            accountRepository.insertAsLastChildOf(node, parent);
        }
        return "redirect:/ledger/accounts";

    }

    // Delete account and descendants
    @RequestMapping(value = "/ledger/accounts/delete", method = RequestMethod.GET)
    public String deleteNode(@RequestParam(name="parentAccountId") Long parentAccountId) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/delete (GET)");
    	logger.info("RequestParam: " + parentAccountId);
    	AccountNode n = accountRepository.findOneById(parentAccountId);
    	accountRepository.removeSubTree(n);
    	//accountRepository.removeSingle(n);
    	return "redirect:/ledger/accounts";
    }

    // Obtain the List of Account Categories
    @RequestMapping(value = "/ledger/accounts/categories", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategories() throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/categories (GET)");
    	return atc.getCategories();

    }

    // Obtain the List of Account Types associated with an Account Category
    @RequestMapping(value = "/ledger/accounts/typesbycategory", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountType> getTypesByCategory(@RequestParam(name="category") String category) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/typesbycategory (GET)");
    	logger.info("RequestParam: " + category);
    	return atc.getTypesByCategory(category);

    }
    
    // Obtain the List of Account Categories associated with an Account Type
    @RequestMapping(value = "/ledger/accounts/categoriesbytype", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountCategory> getCategoriesByType(@RequestParam(name="type") String type) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/categoriesbytype (GET)");
    	logger.info("RequestParam: " + type);
    	return atc.getCategoriesByType(type);

    }

    // Testing placeholder
    @RequestMapping(value = "/ledger/accounts/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(@ModelAttribute("node") AccountNode node, @RequestParam("parentAccountId") Long parentAccountId/*, @RequestParam("method") String method*/) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/test (GET)");
        return "redirect:/ledger/accounts";

    }

}
