package net.flyingfishflash.ledger.transactions.service;

import java.util.List;
import net.flyingfishflash.ledger.transactions.data.Transaction;
import net.flyingfishflash.ledger.transactions.data.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionService {

  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

  private TransactionRepository transactionRepository;

  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public Transaction saveTransaction(Transaction transaction) {

    return transactionRepository.save(transaction);
  }

  public void saveAllTransactions(List<Transaction> transactions) {

    transactionRepository.saveAll(transactions);

  }

  public void deleteAllTransactions() {

    transactionRepository.deleteAll();
  }
}
