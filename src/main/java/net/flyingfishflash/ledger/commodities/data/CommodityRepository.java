package net.flyingfishflash.ledger.commodities.data;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long> {

  public Optional<Commodity> findByGuid(String guid);

  public Optional<List<Commodity>> findByMnemonic(String mnemonic);

  public Optional<List<Commodity>> findByNameSpace(String nameSpace);

  public Optional<Commodity> findByNameSpaceAndMnemonic(String nameSpace, String mnemonic);
}
