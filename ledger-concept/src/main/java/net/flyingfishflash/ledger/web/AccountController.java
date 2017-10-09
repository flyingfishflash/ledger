package net.flyingfishflash.ledger.web;

import java.util.ArrayList;
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

import net.flyingfishflash.ledger.domain.nestedset.Node;
//import net.flyingfishflash.ledger.domain.nestedset.NodeDAOImpl;
import net.flyingfishflash.ledger.domain.nestedset.NodeService;


@Controller
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
    @Autowired
    private NodeService service;
    
    // Display table of accounts that includes options for create/update/delete
    // TODO Reorder an account in the hierarchy, potentially by changing its parent account
    @RequestMapping(value = "/ledger/accounts", method = RequestMethod.GET)
    public String listNodes(Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts");
    	List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
    	// Remove Root node from list of nodes 
    	Iterator<Node> it = nodes.iterator();
		while (it.hasNext()) {
		  Node node = it.next();
		  if (node.isRoot()) {
		    it.remove();
		  }
		}
        model.addAttribute("title", "Accounts");
        model.addAttribute("nodes", nodes);
        return "/ledger/accounts/index";
    }
    
    // Open form for creating a new account
    @RequestMapping(value = "/ledger/accounts/create", method = RequestMethod.GET)
    public String createNode(@RequestParam(name="parentAccountId", defaultValue="1") int parentAccountId, Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/create (GET)");
    	logger.info("RequestParam: " + parentAccountId);
    	// TODO Handle NullPointer Exception if parentId does not exist in nested set
    	Node parent = service.findOneById(parentAccountId);
    	logger.info("Parent: " + parent.getId() + ": " + parent.getName());
        model.addAttribute("title", "Create Account");
        model.addAttribute("parent", parent);
        model.addAttribute("node", new Node());
        return "/ledger/accounts/create";
    }

    // Delete account and descendants
    @RequestMapping(value = "/ledger/accounts/delete", method = RequestMethod.GET)
    public String deleteNode(@RequestParam(name="parentAccountId") int parentAccountId) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/delete (GET)");
    	logger.info("RequestParam: " + parentAccountId);
    	service.deleteNode(service.findOneById(parentAccountId));
        return "redirect:/ledger/accounts";
    }
    
    // Submit form for creating a new account
    // 	POST http://localhost:8080/ledger/accounts/create?parentAccountId=1
    @RequestMapping(value = "/ledger/accounts/create", method = RequestMethod.POST)
    public String saveNode(@ModelAttribute("node") Node node, @RequestParam("parentAccountId") int parentAccountId/*, @RequestParam("method") String method*/) throws Exception {
    	logger.info("@RequestMapping: /ledger/accounts/create (POST)");
        Node parent = service.findOneById(parentAccountId);
        System.out.println(parent.toString());
        System.out.println(parent.getId());
        System.out.println(parent.getName());
        String method = "last";
        if (method.equals("first")) {
            service.addChildAsFirst(parent, node.getName(), 99/*node.getName(), node.getValue()*/);
        } else {
            service.addChildAsLast(parent, node.getName(), 99 /*node.getName(), node.getValue()*/);
        }
        return "redirect:/ledger/accounts";

    }
    

}
