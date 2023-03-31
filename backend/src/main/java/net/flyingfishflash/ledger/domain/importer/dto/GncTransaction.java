package net.flyingfishflash.ledger.domain.importer.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GncTransaction {

  private String guid;
  private String currency;
  private LocalDate datePosted;
  private String description;
  private String num;
  private List<GncSplit> gncSplitList = new ArrayList<>();
  private boolean isTemplate = false;
  private String scheduledActionUID = null;

  public GncTransaction(String name) {
    setDescription(name);
  }

  public void setGuid(String guid) {
    this.guid = guid;
    for (GncSplit gncSplit : gncSplitList) {
      gncSplit.setTransactionGuid(guid);
    }
  }

  public String getGuid() {
    return guid;
  }

  public List<GncSplit> getSplits() {
    return gncSplitList;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public LocalDate getDatePosted() {
    return datePosted;
  }

  public void setDatePosted(LocalDate datePosted) {
    this.datePosted = datePosted;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description.trim();
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public void addSplit(GncSplit gncSplit) {
    gncSplit.setTransactionGuid(this.guid);
    gncSplitList.add(gncSplit);
  }

  public boolean isTemplate() {
    return isTemplate;
  }

  public void setTemplate(boolean isTemplate) {
    this.isTemplate = isTemplate;
  }

  @Override
  public String toString() {
    return "GncTransactionSparse{"
        + "guid='"
        + guid
        + '\''
        + ", currency='"
        + currency
        + '\''
        + ", datePosted="
        + datePosted
        + ", description='"
        + description
        + '\''
        + ", num='"
        + num
        + '\''
        + ", gncSplitList="
        + gncSplitList
        + ", isTemplate="
        + isTemplate
        + ", scheduledActionUID='"
        + scheduledActionUID
        + '\''
        + '}';
  }
}
