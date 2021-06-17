package net.flyingfishflash.ledger.accounts.data.dto;

/*
{
    "code": "2",
    "description": "Financial Assets Description",
    "hidden": true,
    "mode" : "last_child",
    "name": "Financial Assets",
    "parentId": 2,
    "placeholder": true,
    "siblingId" : 0,
    "taxRelated": true
}
*/

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

import net.flyingfishflash.ledger.foundation.validators.Enum;

public class AccountCreateRequest {

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String code;

  @Size(max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String description;

  @NotNull public Boolean hidden;

  @NotEmpty
  @Enum(
      enumClass = pl.exsio.nestedj.delegate.NestedNodeHierarchyManipulator.Mode.class,
      ignoreCase = true)
  @Schema(
      description = "Node manipulator mode.",
      allowableValues = "FIRST_CHILD, LAST_CHILD, PREV_SIBLING, NEXT_SIBLING",
      required = true)
  public String mode;

  @Size(max = 2048)
  @NotEmpty
  @Schema(required = true)
  public String name;

  @Size(max = 4096)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String note;

  @NotNull
  @Positive
  @Schema(required = true)
  public Long parentId;

  @NotNull public Boolean placeholder;

  @Min(2)
  @Schema(description = "Required if mode is PREV_SIBLING or NEXT_SIBLING. Must be > 1.")
  public Long siblingId;

  @NotNull public Boolean taxRelated;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public Boolean getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(Boolean placeholder) {
    this.placeholder = placeholder;
  }

  public Long getSiblingId() {
    return siblingId;
  }

  public void setSiblingId(Long siblingId) {
    this.siblingId = siblingId;
  }

  public Boolean getTaxRelated() {
    return taxRelated;
  }

  public void setTaxRelated(Boolean taxRelated) {
    this.taxRelated = taxRelated;
  }

  @Override
  public String toString() {
    return "AccountCreateRequest{"
        + "code='"
        + code
        + '\''
        + ", description='"
        + description
        + '\''
        + ", hidden="
        + hidden
        + ", mode='"
        + mode
        + '\''
        + ", name='"
        + name
        + '\''
        + ", note='"
        + note
        + '\''
        + ", parentId="
        + parentId
        + ", placeholder="
        + placeholder
        + ", siblingId="
        + siblingId
        + ", taxRelated="
        + taxRelated
        + '}';
  }
}
