package net.flyingfishflash.ledger.books;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import net.flyingfishflash.ledger.books.data.ActiveBook;
import net.flyingfishflash.ledger.books.data.Book;
import net.flyingfishflash.ledger.books.data.dto.BookMapper;

@Configuration
@ComponentScan(
    basePackages = {
      "net.flyingfishflash.ledger.foundation",
      "net.flyingfishflash.ledger.books",
    })
@EntityScan(basePackageClasses = {Book.class})
public class BookConfiguration {

  @Bean
  @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public ActiveBook activeBook() {
    return new ActiveBook();
  }

  @Bean
  public BookMapper bookMapper() {
    return new BookMapper();
  }
}
