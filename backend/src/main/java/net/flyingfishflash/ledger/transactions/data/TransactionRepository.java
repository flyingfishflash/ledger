package net.flyingfishflash.ledger.transactions.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.books.data.Book;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Optional<Transaction> findByBookAndGuid(Book book, String guid);
}
