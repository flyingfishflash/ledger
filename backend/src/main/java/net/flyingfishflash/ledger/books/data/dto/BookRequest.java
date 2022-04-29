package net.flyingfishflash.ledger.books.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record BookRequest(@NotBlank @Size(max = 50) String name) {}
