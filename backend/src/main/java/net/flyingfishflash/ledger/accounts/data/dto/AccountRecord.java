package net.flyingfishflash.ledger.accounts.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import net.flyingfishflash.ledger.accounts.data.AccountCategory;
import net.flyingfishflash.ledger.accounts.data.AccountType;

/**
 * Record (DTO) class representing an {@link net.flyingfishflash.ledger.accounts.data.Account
 * Account}
 */
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
