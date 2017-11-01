package net.flyingfishflash.ledger.web;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountRepository;


@Controller
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
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
    	logger.info(parent.toString());
    	AccountNode n = new AccountNode();
    	logger.info(n.toString());
        model.addAttribute("title", "Create Account");
        model.addAttribute("parent", parent);
        model.addAttribute("node", new AccountNode());
        return "/ledger/accounts/create";
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
    
    // sloppy test URL
    @RequestMapping(value = "/ledger/accounts/test", method = RequestMethod.GET)
    public String test(@ModelAttribute("node") AccountNode node, @RequestParam("parentAccountId") Long parentAccountId/*, @RequestParam("method") String method*/) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/test (POST)");
        AccountNode parent = accountRepository.findOneById(parentAccountId);
        //accountRepository.deriveLongName(parent);
        return "redirect:/ledger/accounts";

    }

}
