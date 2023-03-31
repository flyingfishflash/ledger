package net.flyingfishflash.ledger.domain.books;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.flyingfishflash.ledger.domain.books.data.dto.BookMapper;

@Configuration
public class BookConfiguration {

  @Bean
  public BookMapper bookMapper() {
    return new BookMapper();
  }
}
