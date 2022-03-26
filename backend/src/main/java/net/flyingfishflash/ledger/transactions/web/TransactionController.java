package net.flyingfishflash.ledger.transactions.web;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.transactions.service.TransactionService;

@RestController
@Validated
@RequestMapping("api/v1/ledger/transactions")
public class TransactionController {

  private TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }
}
