package net.flyingfishflash.ledger.domain.transactions.web;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.domain.transactions.service.TransactionService;

@RestController
@Validated
@RequestMapping("${config.application.api-v1-url-path}/transactions")
public class TransactionController {

  @SuppressWarnings("unused")
  private TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }
}
