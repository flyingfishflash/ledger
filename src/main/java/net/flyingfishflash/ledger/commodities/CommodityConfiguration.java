package net.flyingfishflash.ledger.commodities;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CommodityConfiguration {

  @PersistenceContext
  EntityManager entityManager;

}
