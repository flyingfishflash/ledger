package net.flyingfishflash.ledger.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import net.flyingfishflash.ledger.domain.nestedset.Node;
import net.flyingfishflash.ledger.domain.nestedset.NodeService;

@Controller
public class HomeController {

    @Autowired
    private NodeService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() throws Exception {
        return "index";
    }

    // display table of accounts with options to create/update/delete
    @RequestMapping(value = "/ledger/accounts", method = RequestMethod.GET)
    public String nodes(Model model) throws Exception {
        List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
        model.addAttribute("title", "Accounts");
        model.addAttribute("nodes", nodes);
        model.addAttribute("node", new Node());
        return "/ledger/accounts/index";
    }

    @RequestMapping(value = "/ledger/accounts/add", method = RequestMethod.POST)
    public String addNode(@ModelAttribute("node") Node node, @RequestParam("currentNodeId") int id, @RequestParam("method") String method) throws Exception {

        Node parent = service.findOneById(id);
        if (method.equals("first")) {
            service.addChildAsFirst(parent, node.getName(), node.getValue());
        } else {
            service.addChildAsLast(parent, node.getName(), node.getValue());
        }

        return "redirect:/";
    }


    
    
    
}
