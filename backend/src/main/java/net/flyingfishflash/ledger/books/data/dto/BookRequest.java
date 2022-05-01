package net.flyingfishflash.ledger.books.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** DTO representing an API request to create or patch a book of accounts. */
public record BookRequest(@NotBlank @Size(max = 50) String name) {}
