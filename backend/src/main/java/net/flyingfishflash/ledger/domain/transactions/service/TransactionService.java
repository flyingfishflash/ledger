package net.flyingfishflash.ledger.domain.transactions.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.domain.transactions.data.Transaction;
import net.flyingfishflash.ledger.domain.transactions.data.TransactionRepository;

@Service
@Transactional
public class TransactionService {

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
