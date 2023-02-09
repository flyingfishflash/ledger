package net.flyingfishflash.ledger.domain.books.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {

  // public Optional<Book> findByGuid(String guid);
}
