package net.flyingfishflash.ledger.importer.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record GnucashFileImportBookRequest(@NotNull @Min(1) Long bookId) {}
