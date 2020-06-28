package net.flyingfishflash.ledger.prices.data;

import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PriceRepository extends PagingAndSortingRepository<Price, Long> {

  public Optional<Price> findByGuid(String guid);
}
