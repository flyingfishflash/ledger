package net.flyingfishflash.ledger.domain;

/*
{
    "code": "2",
    "description": "Financial Assets Account Description",
    "hidden": true,
    "mode" : "lastchildof",
    "name": "Financial Assets",
    "parentId": 2,
    "placeholder": true,
    "siblingId" : null,
    "taxRelated": true
}
*/

public class CreateAccountDto {

  public String code;
  public String description;
  public Boolean hidden;
  public String mode;
  public String name;
  public Long parentId;
  public Boolean placeholder;
  public Long siblingId;
  public Boolean taxRelated;

  public CreateAccountDto() {}

  @Override
  public String toString() {
    return "AccountCreateDto{" +
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
