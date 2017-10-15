package net.flyingfishflash.ledger.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) throws Exception {
    	logger.info("@RequestMapping: /");
        model.addAttribute("title", "Ledger Home");
        return "redirect:ledger";
    }

    @RequestMapping(value = "/ledger", method = RequestMethod.GET)
    public String ledger(Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger");
        model.addAttribute("title", "Ledger Dashboard");
        return "/ledger/index";
    }

    @RequestMapping(value = "/ledger/notes", method = RequestMethod.GET)
    public String notes(Model model) throws Exception {
    	logger.info("@RequestMapping: /ledger/notes");
        model.addAttribute("title", "Ledger Notes");
        return "/ledger/notes/index";
    }

    
    
    
}
