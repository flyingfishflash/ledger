package net.flyingfishflash.ledger.web;

import java.util.ArrayList;
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
import net.flyingfishflash.ledger.domain.nestedset.NodeDAOImpl;
import net.flyingfishflash.ledger.domain.nestedset.NodeService;


@Controller
public class AccountController {
	
    private static final Logger logger = LoggerFactory.getLogger(NodeDAOImpl.class);
	
    @Autowired
    private NodeService service;

    // display table of accounts with options to create/update/delete
    @RequestMapping(value = "/ledger/accounts", method = RequestMethod.GET)
    public String nodes(Model model) throws Exception {
        List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
        model.addAttribute("title", "Accounts");
        model.addAttribute("nodes", nodes);
        model.addAttribute("node", new Node());
        return "/ledger/accounts/index";
    }

    @RequestMapping(value = "/ledger/accounts/create", method = RequestMethod.GET)
    public String createNode(@RequestParam(name="parentId", defaultValue="1") int parentId, Model model) throws Exception {
    	logger.info("RequestParam: " + parentId);
    	// TODO NullPointer Exception if parentId does not exist in nested set
    	Node parent = service.findOneById(parentId);
    	logger.info("Parent: " + parent.getId() + ": " + parent.getName());
        //List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
        model.addAttribute("title", "Create Account");
        model.addAttribute("parent");
        model.addAttribute("node", new Node());
       
        return "/ledger/accounts/create";
    }
    
    /*
    	POST http://localhost:8080/ledger/accounts/add
    	Content-Type: application/x-www-form-urlencoded
    	currentNodeId=1&method=first
    */
    @RequestMapping(value = "/ledger/accounts/add", method = RequestMethod.POST)
    public String addNode(@ModelAttribute("node") Node node, @RequestParam("currentNodeId") int id, @RequestParam("method") String method) throws Exception {
        Node parent = service.findOneById(id);
        System.out.println(parent.toString());
        System.out.println(parent.getId());
        System.out.println(parent.getName());

        if (method.equals("first")) {
            service.addChildAsFirst(parent, "new node first child", 99/*node.getName(), node.getValue()*/);
        } else {
            service.addChildAsLast(parent, "new node last child", 99 /*node.getName(), node.getValue()*/);
        }

        return "redirect:/ledger/accounts";

    }
    

}
