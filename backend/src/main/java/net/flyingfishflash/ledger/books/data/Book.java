package net.flyingfishflash.ledger.books.data;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {

  @Id
  @SequenceGenerator(name = "book_id_seq", sequenceName = "book_seq", allocationSize = 1)
  @GeneratedValue(generator = "book_id_seq")
  private Long id;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdInstant;

  @NotBlank
  @Size(max = 50)
  @Column(unique = true)
  private String name;

  public Book() {}

  public Book(String bookName) {
    // this.createdInstant = Instant.now();
    // this.guid = guid;
    this.name = bookName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Instant getCreatedInstant() {
    return createdInstant;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Book{"
        + "id="
        + id
        + ", createdInstant="
        + createdInstant
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
