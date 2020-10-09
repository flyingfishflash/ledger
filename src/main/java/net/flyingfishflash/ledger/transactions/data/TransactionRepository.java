package net.flyingfishflash.ledger.transactions.data;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

  Optional<Transaction> findByGuid(String guid);
}
