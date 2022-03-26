package net.flyingfishflash.ledger.commodities.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long> {

  Optional<Commodity> findByGuid(String guid);

  Optional<List<Commodity>> findByMnemonic(String mnemonic);

  Optional<List<Commodity>> findByNamespace(String namespace);

  Optional<Commodity> findByNamespaceAndMnemonic(String namespace, String mnemonic);
}
