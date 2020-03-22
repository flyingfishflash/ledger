package net.flyingfishflash.ledger.prices;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.flyingfishflash.ledger.commodities.data.Commodity;
import net.flyingfishflash.ledger.prices.data.Price;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EntityScan(basePackageClasses = {Price.class, Commodity.class})
public class PriceConfiguration {

  //@PersistenceContext EntityManager entityManager;
}
