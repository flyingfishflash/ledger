package net.flyingfishflash.ledger.prices;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.prices.data.Price;

@Configuration
@ComponentScan(
    basePackages = {
      "net.flyingfishflash.ledger.foundation",
      "net.flyingfishflash.ledger.prices",
    })
@EntityScan(
    basePackageClasses = {
      Price.class,
      Commodity.class,
    })
public class PriceConfiguration {

  // @PersistenceContext EntityManager entityManager;
}
