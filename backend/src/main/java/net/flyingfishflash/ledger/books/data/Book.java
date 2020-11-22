package net.flyingfishflash.ledger.books.data;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "book")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
