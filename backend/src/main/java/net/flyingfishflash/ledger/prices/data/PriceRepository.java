package net.flyingfishflash.ledger.prices.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PriceRepository extends JpaRepository<Price, Long> {

  Optional<Price> findByGuid(String guid);
}
