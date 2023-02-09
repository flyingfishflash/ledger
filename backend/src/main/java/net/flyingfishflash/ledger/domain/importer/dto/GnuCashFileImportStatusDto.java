package net.flyingfishflash.ledger.domain.importer.dto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GnuCashFileImportStatusDto {

  private class Component {
    private String componentName;
    private Integer gncCount = 0;
    private Integer sentToAdapter = 0;
    private Integer persisted = 0;
    private Integer ignoredTemplates = 0;
    private Integer ignoredCurrencies = 0;

    Component(String componentName) {
      this.componentName = componentName;
    }

    public String getComponentName() {
      return componentName;
    }

    public void setComponentName(String componentName) {
      this.componentName = componentName;
    }

    public Integer getGncCount() {
      return gncCount;
    }

    public void setGncCount(Integer gncCount) {
      this.gncCount = gncCount;
    }

    public Integer getSentToAdapter() {
      return sentToAdapter;
    }

    public void setSentToAdapter(Integer sentToAdapter) {
      this.sentToAdapter = sentToAdapter;
    }

    public Integer getPersisted() {
      return persisted;
    }

    public void setPersisted(Integer persisted) {
      this.persisted = persisted;
    }

    public Integer getIgnoredTemplates() {
      return ignoredTemplates;
    }

    public void setIgnoredTemplates(Integer ignoredTemplates) {
      this.ignoredTemplates = ignoredTemplates;
    }

    public Integer getIgnoredCurrencies() {
      return ignoredCurrencies;
    }

    public void setIgnoredCurrencies(Integer ignoredCurrencies) {
      this.ignoredCurrencies = ignoredCurrencies;
    }
  }

  List<Component> components = new ArrayList<>(4);

  String status;
}
