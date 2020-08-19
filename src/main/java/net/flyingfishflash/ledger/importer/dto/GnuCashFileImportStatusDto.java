package net.flyingfishflash.ledger.importer.dto;

import java.util.ArrayList;
import java.util.List;

public class GnuCashFileImportStatusDto {

  private class Component {
    public String component;
    public Integer gncCount = 0;
    public Integer sentToAdapter = 0;
    public Integer persisted = 0;
    public Integer ignoredTemplates = 0;
    public Integer ignoredCurrencies = 0;

    Component(String component) {
      this.component = component;
    }

  }

  List<Component> components = new ArrayList<>(4);

  String status;

}
