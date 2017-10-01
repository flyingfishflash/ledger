package net.flyingfishflash.ledger.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public List<Node> nodes(Model model) throws Exception {
        List<Node> nodes = new ArrayList<Node>(service.findWholeTree());
        //model.addAttribute("nodes", nodes);
        //model.addAttribute("node", new Node());
        //return "nodes_tree";
        return nodes;
    }

}
