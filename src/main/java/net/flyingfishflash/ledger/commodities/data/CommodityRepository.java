package net.flyingfishflash.ledger.commodities.data;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommodityRepository extends PagingAndSortingRepository<Commodity, Long> {

  public Optional<Commodity> findByGuid(String guid);

  public Optional<List<Commodity>> findByMnemonic(String mnemonic);

  public Optional<List<Commodity>> findByNameSpace(String nameSpace);

  public Optional<Commodity> findByNameSpaceAndMnemonic(String nameSpace, String mnemonic);
}
