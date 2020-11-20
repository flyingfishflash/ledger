package net.flyingfishflash.ledger.books.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BookRequest {

  @NotBlank
  @Size(max = 50)
  private String name;

  public BookRequest() {}

  public BookRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "BookRequest{" + "name='" + name + '\'' + '}';
  }
}
