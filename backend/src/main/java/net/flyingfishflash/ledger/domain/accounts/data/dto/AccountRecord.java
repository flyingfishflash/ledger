package net.flyingfishflash.ledger.domain.accounts.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import net.flyingfishflash.ledger.domain.accounts.data.Account;
import net.flyingfishflash.ledger.domain.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.domain.accounts.data.AccountType;

/** Record (DTO) class representing an {@link Account Account} */
public record AccountRecord(
    AccountCategory accountCategory,
    AccountType accountType,
    @Size(max = 2048)
        @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "may be null, must not be an empty string, must not consist only of spaces")
        String code,
    @Size(max = 2048)
        @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "may be null, must not be an empty string, must not consist only of spaces")
        String description,
    @Size(max = 2048) @NotBlank String discriminator,
    @Size(max = 2048) @NotBlank String guid,
    @NotNull Boolean hidden,
    @Positive @NotNull Long id,
    @Size(max = 2048) @NotBlank String longName,
    @Size(max = 2048) @NotBlank String name,
    @Size(max = 4096)
        @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "may be null, must not be an empty string, must not consist only of spaces")
        String note,
    @Positive @NotNull Long parentId,
    @NotNull Boolean placeholder,
    @NotNull Boolean taxRelated,
    @Positive Long treeLeft,
    @Positive Long treeLevel,
    @Positive Long treeRight) {}
