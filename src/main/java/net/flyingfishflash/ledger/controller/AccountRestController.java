package net.flyingfishflash.ledger.controller;

import net.flyingfishflash.ledger.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("ledger/api/v1/")
public class AccountRestController {

    private static final Logger logger = LoggerFactory.getLogger(AccountRestController.class);

    @Autowired
    private AccountService accountService;
}
