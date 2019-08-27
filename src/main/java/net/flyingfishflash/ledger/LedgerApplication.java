package net.flyingfishflash.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("net.flyingfishflash.ledger")
public class LedgerApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(LedgerApplication.class, args);
  }
}
