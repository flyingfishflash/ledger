package net.flyingfishflash.ledger.domain.importer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GnucashFileImportBookRequest(@NotNull @Min(1) Long bookId) {}
