package net.flyingfishflash.ledger.books.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** DTO representing an API request to create or patch a book of accounts. */
public record BookRequest(@NotBlank @Size(max = 50) String name) {}
