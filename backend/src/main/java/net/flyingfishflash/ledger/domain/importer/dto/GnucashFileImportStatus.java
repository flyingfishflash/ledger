package net.flyingfishflash.ledger.domain.importer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@JsonIgnoreProperties({
  "targetClass",
  "targetSource",
  "targetObject",
  "advisors",
  "frozen",
  "exposeProxy",
  "preFiltered",
  "proxiedInterfaces",
  "proxyTargetClass"
})
@SuppressWarnings("unused")
public class GnucashFileImportStatus implements Serializable {

  private static final Logger logger = LoggerFactory.getLogger(GnucashFileImportStatus.class);

  private class Component implements Serializable {
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

    @Override
    public String toString() {
      return "Component{"
          + "component='"
          + componentName
          + '\''
          + ", gncCount="
          + gncCount
          + ", sentToAdapter="
          + sentToAdapter
          + ", persisted="
          + persisted
          + ", ignoredTemplates="
          + ignoredTemplates
          + ", ignoredCurrencies="
          + ignoredCurrencies
          + '}';
    }
  }

  private final List<Component> components = new ArrayList<>(4);

  String status;

  public GnucashFileImportStatus() {
    this.components.add(new Component("Commodities"));
    this.components.add(new Component("Accounts"));
    this.components.add(new Component("Transactions"));
    this.components.add(new Component("Prices"));
  }

  public String getStatus() {
    return status;
  }

  public void setStatusComplete() {
    this.status = "Complete";
  }

  public List<Component> getComponents() {
    return this.components;
  }

  @PostConstruct
  public void init() {
    logger.info("sessionScopedBean: init");
  }

  @PreDestroy
  public void close() {
    logger.info("sessionScopedBean: close");
  }

  public void invoke() {
    logger.info("sessionScopedBean: invoke");
  }

  @JsonIgnore
  public int getCommoditiesGncCount() {
    return this.components.get(0).gncCount;
  }

  @JsonIgnore
  public void setCommoditiesGncCount(int commoditiesGncCount) {
    this.components.get(0).gncCount = commoditiesGncCount;
  }

  @JsonIgnore
  public int getCommoditiesSentToAdapter() {
    return this.components.get(0).sentToAdapter;
  }

  @JsonIgnore
  public void setCommoditiesSentToAdapter(int commoditiesSentToAdapter) {
    this.components.get(0).sentToAdapter = commoditiesSentToAdapter;
  }

  @JsonIgnore
  public int getCommoditiesPersisted() {
    return this.components.get(0).persisted;
  }

  @JsonIgnore
  public void setCommoditiesPersisted(int commoditiesPersisted) {
    this.components.get(0).persisted = commoditiesPersisted;
  }

  @JsonIgnore
  public int getCommoditiesIgnoredTotal() {
    return 0;
  }

  /*
    @JsonIgnore
    public void setCommoditiesIgnoredTotal(int commoditiesIgnoredTotal) {}
  */

  @JsonIgnore
  public int getCommoditiesIgnoredTemplates() {
    return this.components.get(0).ignoredTemplates;
  }

  @JsonIgnore
  public void setCommoditiesIgnoredTemplates(int commoditiesIgnoredTemplates) {
    this.components.get(0).ignoredTemplates = commoditiesIgnoredTemplates;
  }

  @JsonIgnore
  public int getCommoditiesIgnoredCurrencies() {
    return this.components.get(0).ignoredCurrencies;
  }

  @JsonIgnore
  public void setCommoditiesIgnoredCurrencies(int commoditiesIgnoredCurrencies) {
    this.components.get(0).ignoredCurrencies = commoditiesIgnoredCurrencies;
  }

  @JsonIgnore
  public int getAccountsGncCount() {
    return this.components.get(1).gncCount;
  }

  @JsonIgnore
  public void setAccountsGncCount(int accountsGncCount) {
    this.components.get(1).gncCount = accountsGncCount;
  }

  @JsonIgnore
  public int getAccountsSentToAdapter() {
    return this.components.get(1).sentToAdapter;
  }

  @JsonIgnore
  public void setAccountsSentToAdapter(int accountsSentToAdapter) {
    this.components.get(1).sentToAdapter = accountsSentToAdapter;
  }

  @JsonIgnore
  public int getAccountsPersisted() {
    return this.components.get(1).persisted;
  }

  @JsonIgnore
  public void setAccountsPersisted(int accountsPersisted) {
    this.components.get(1).persisted = accountsPersisted;
  }

  @JsonIgnore
  public int getTransactionsGncCount() {
    return this.components.get(2).gncCount;
  }

  @JsonIgnore
  public void setTransactionsGncCount(int transactionsGncCount) {
    this.components.get(2).gncCount = transactionsGncCount;
  }

  @JsonIgnore
  public int getTransactionsTemplates() {
    return this.components.get(2).ignoredTemplates;
  }

  @JsonIgnore
  public void setTransactionsTemplates(int transactionsTemplates) {
    this.components.get(2).ignoredTemplates = transactionsTemplates;
  }

  @JsonIgnore
  public int getTransactionsSentToAdapter() {
    return this.components.get(2).sentToAdapter;
  }

  @JsonIgnore
  public void setTransactionsSentToAdapter(int transactionsSentToAdapter) {
    this.components.get(2).sentToAdapter = transactionsSentToAdapter;
  }

  @JsonIgnore
  public int getTransactionsPersisted() {
    return this.components.get(2).persisted;
  }

  @JsonIgnore
  public void setTransactionsPersisted(int transactionsPersisted) {
    this.components.get(2).persisted = transactionsPersisted;
  }

  @JsonIgnore
  public int getPricesGncCount() {
    return this.components.get(3).gncCount;
  }

  @JsonIgnore
  public void setPricesGncCount(int pricesGncCount) {
    this.components.get(3).gncCount = pricesGncCount;
  }

  @JsonIgnore
  public int getPricesSentToAdapter() {
    return this.components.get(3).sentToAdapter;
  }

  @JsonIgnore
  public void setPricesSentToAdapter(int pricesSentToAdapter) {
    this.components.get(3).sentToAdapter = pricesSentToAdapter;
  }

  @JsonIgnore
  public int getPricesPersisted() {
    return this.components.get(3).persisted;
  }

  @JsonIgnore
  public void setPricesPersisted(int pricesPersisted) {
    this.components.get(3).persisted = pricesPersisted;
  }

  public String toJson() throws JsonProcessingException {
    var mapper = new ObjectMapper();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
  }

  @Override
  public String toString() {
    return "GnucashFileImportStatus{"
        + "components="
        + components.toString()
        + ", status='"
        + status
        + '\''
        + '}';
  }

  /*  @Override
    public String toString() {
  */
  /*    return "GnucashFileImportStatus{"
  + "status='"
  + status
  + '\''
  + ", commoditiesGncCount="
  + commoditiesGncCount
  + ", commoditiesSentToAdapter="
  + commoditiesSentToAdapter
  + ", commoditiesPersisted="
  + commoditiesPersisted
  + ", commoditiesIgnoredTotal="
  + commoditiesIgnoredTotal
  + ", getCommoditiesIgnoredTemplates="
  + commoditiesIgnoredTemplates
  + ", getCommiditiesIgnoredCurrencies="
  + commoditiesIgnoredCurrencies
  + ", accountsGncCount="
  + accountsGncCount
  + ", accountsSentToAdapter="
  + accountsSentToAdapter
  + ", accountsPersisted="
  + accountsPersisted
  + ", transactionsGncCount="
  + transactionsGncCount
  + ", transactionsTemplates="
  + transactionsTemplates
  + ", transactionsSentToAdapter="
  + transactionsSentToAdapter
  + ", transactionsPersisted="
  + transactionsPersisted
  + ", pricesGncCount="
  + pricesGncCount
  + ", pricesSentToAdapter="
  + pricesSentToAdapter
  + ", pricesPersisted="
  + pricesPersisted
  + '}';*/
  /*
    return "filler";
  }*/
}
