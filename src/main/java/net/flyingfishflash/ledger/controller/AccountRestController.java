package net.flyingfishflash.ledger.controller;

import net.flyingfishflash.ledger.domain.AccountNode;
import net.flyingfishflash.ledger.domain.AccountNodeDto;
import net.flyingfishflash.ledger.service.rest.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;


@RestController
@RequestMapping("ledger/api/v1/")
public class AccountRestController {

    private static final Logger logger = LoggerFactory.getLogger(AccountRestController.class);

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "accounts", method = RequestMethod.GET)
    public ResponseEntity<Iterable<AccountNode>> getAllAccounts() throws Throwable {
        return accountService.getAllAccounts();
    }

    // List One Account
    @RequestMapping(value = "accounts/{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountNodeDto> getSingleAccountNodeRespons(@PathVariable Long id) throws Throwable {
        return accountService.getSingleAccountNodeResponse(id);

    }

}
