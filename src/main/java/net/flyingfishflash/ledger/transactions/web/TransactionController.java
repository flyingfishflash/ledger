package net.flyingfishflash.ledger.transactions.web;

import net.flyingfishflash.ledger.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("api/v1/ledger/transactions")
public class TransactionController {

  private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

  private TransactionService transactionService;


  public TransactionController(
      TransactionService transactionService) {
    this.transactionService = transactionService;
  }
}
