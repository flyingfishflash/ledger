package net.flyingfishflash.ledger.books.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

  // public Optional<Book> findByGuid(String guid);
}
