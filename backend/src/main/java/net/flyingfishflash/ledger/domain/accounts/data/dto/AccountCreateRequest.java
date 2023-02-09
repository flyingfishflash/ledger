package net.flyingfishflash.ledger.domain.accounts.data.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import net.flyingfishflash.ledger.core.validators.Enum;
import net.flyingfishflash.ledger.domain.accounts.data.nestedset.delegate.NestedNodeHierarchyManipulator;
import net.flyingfishflash.ledger.domain.accounts.web.AccountController;

/**
 * Record (DTO) class representing an API request to create a new account
 *
 * <p>{@link AccountController#createAccount(String, AccountCreateRequest) Controller Method} <br>
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
    @NotNull @Positive @Schema(requiredMode = REQUIRED) Long bookId,
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
            requiredMode = REQUIRED)
        String mode,
    @Size(max = 2048) @NotEmpty @Schema(requiredMode = REQUIRED) String name,
    @Size(max = 4096)
        @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "may be null, must not be an empty string, must not consist only of spaces")
        String note,
    @NotNull @Positive @Schema(requiredMode = REQUIRED) Long parentId,
    @NotNull Boolean placeholder,
    @Min(2) @Schema(description = "Required if mode is PREV_SIBLING or NEXT_SIBLING. Must be > 1.")
        Long siblingId,
    @NotNull Boolean taxRelated) {}
