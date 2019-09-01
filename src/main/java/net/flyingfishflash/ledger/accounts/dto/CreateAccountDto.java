package net.flyingfishflash.ledger.accounts.dto;

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
import net.flyingfishflash.ledger.utilities.validators.Enum;

public class CreateAccountDto {

  @Size(min = 1, max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "The account code may be null. It must not be an empty string, or consist only of spaces.")
  public String code;

  @Size(min = 1, max = 2048)
  @Pattern(
      regexp = "^(?!\\s*$).+",
      message = "The account description may be null. It must not be an empty string, or consist only of spaces.")
  public String description;

  @NotNull public Boolean hidden;

  @NotEmpty
  @Enum(
      enumClass = pl.exsio.nestedj.delegate.NestedNodeHierarchyManipulator.Mode.class,
      ignoreCase = true)
  public String mode;

  @Size(min = 1, max = 2048)
  @NotEmpty
  public String name;

  @Positive @NotNull public Long parentId;

  @NotNull public Boolean placeholder;

  @Min(2)
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
        ", parentId=" + parentId +
        ", placeholder=" + placeholder +
        ", siblingId=" + siblingId +
        ", taxRelated=" + taxRelated +
        '}';
  }
}
