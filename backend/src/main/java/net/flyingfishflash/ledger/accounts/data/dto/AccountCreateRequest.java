package net.flyingfishflash.ledger.accounts.data.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

import net.flyingfishflash.ledger.accounts.data.nestedset.delegate.NestedNodeHierarchyManipulator;
import net.flyingfishflash.ledger.foundation.validators.Enum;

/**
 * Record (DTO) class representing an API request to create a new account
 *
 * <p>{@link net.flyingfishflash.ledger.accounts.web.AccountController#createAccount(String,
 * AccountCreateRequest) Controller Method} <br>
 *
 * <p>Sample JSON:
 *
 * <pre>
 * {"bookId":"1",
 *  "code":"2",
 *  "description":"Financial Assets Description",
 *  "hidden":true,
 *  "mode":"last_child",
 *  "name":"Financial Assets",
 *  "parentId":2,
 *  "placeholder":true,
 *  "siblingId":0,
 *  "taxRelated":true}
 * </pre>
 */
public record AccountCreateRequest(
    @NotNull @Positive @Schema(required = true) Long bookId,
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
    @NotNull Boolean hidden,
    @NotEmpty
        @Enum(enumClass = NestedNodeHierarchyManipulator.Mode.class, ignoreCase = true)
        @Schema(
            description = "Node manipulator mode.",
            allowableValues = "FIRST_CHILD, LAST_CHILD, PREV_SIBLING, NEXT_SIBLING",
            required = true)
        String mode,
    @Size(max = 2048) @NotEmpty @Schema(required = true) String name,
    @Size(max = 4096)
        @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "may be null, must not be an empty string, must not consist only of spaces")
        String note,
    @NotNull @Positive @Schema(required = true) Long parentId,
    @NotNull Boolean placeholder,
    @Min(2) @Schema(description = "Required if mode is PREV_SIBLING or NEXT_SIBLING. Must be > 1.")
        Long siblingId,
    @NotNull Boolean taxRelated) {}
