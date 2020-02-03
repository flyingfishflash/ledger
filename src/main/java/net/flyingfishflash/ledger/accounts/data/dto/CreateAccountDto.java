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

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import net.flyingfishflash.ledger.utilities.validators.Enum;

public class CreateAccountDto {

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
  @ApiModelProperty(
      value = "Node manipulator mode.",
      allowableValues = "FIRST_CHILD, LAST_CHILD, PREV_SIBLING, NEXT_SIBLING",
      required = true)
  public String mode;

  @Size(max = 2048)
  @NotEmpty
  @ApiModelProperty(required = true)
  public String name;

  @Size(max = 4096)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "may be null, must not be an empty string, must not consist only of spaces")
  public String note;

  @NotNull
  @Positive
  @ApiModelProperty(required = true)
  public Long parentId;

  @NotNull public Boolean placeholder;

  @Min(2)
  @ApiModelProperty(value = "Required if mode is PREV_SIBLING or NEXT_SIBLING. Must be > 1.")
  public Long siblingId;

  @NotNull public Boolean taxRelated;

  public CreateAccountDto() {}

  @Override
  public String toString() {
    return "CreateAccountDto{" +
        "code='" + code + '\'' +
        ", description='" + description + '\'' +
        ", hidden=" + hidden +
        ", mode='" + mode + '\'' +
        ", name='" + name + '\'' +
        ", note='" + note + '\'' +
        ", parentId=" + parentId +
        ", placeholder=" + placeholder +
        ", siblingId=" + siblingId +
        ", taxRelated=" + taxRelated +
        '}';
  }
}
