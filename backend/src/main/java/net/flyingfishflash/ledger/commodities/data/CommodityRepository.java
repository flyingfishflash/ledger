package net.flyingfishflash.ledger.commodities.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.flyingfishflash.ledger.books.data.Book;

@Repository
@Transactional
public interface CommodityRepository extends JpaRepository<Commodity, Long> {

  Optional<Commodity> findByGuid(String guid);

  Optional<List<Commodity>> findByMnemonic(String mnemonic);

  Optional<List<Commodity>> findByNamespace(String namespace);

  Optional<Commodity> findByBookAndNamespaceAndMnemonic(
      Book book, String namespace, String mnemonic);
}
