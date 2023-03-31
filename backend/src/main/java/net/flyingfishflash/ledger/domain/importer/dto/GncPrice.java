package net.flyingfishflash.ledger.domain.importer.dto;

import java.sql.Timestamp;
// import net.flyingfishflash.ledger.importer.utilities.TimestampHelper;

public class GncPrice /*extends BaseModel*/ {

  private String guid;
  private String commodityId;
  private String currencyId;
  private Timestamp mDate;
  private String source;
  private String type;
  /* Transient, converted to numerator/denominator via adapter */
  private String price;
  /* Transient, used only to look up previously imported commodity */
  private String commodityGuid;
  private String commodityNamespace;
  /* Transient, used only to look up previously imported commodity (in currency namespace) */
  private String currencyGuid;
  private String currencyNamespace;

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getCommodityUID() {
    return commodityGuid;
  }

  public String getCommodityNamespace() {
    return commodityNamespace;
  }

  public void setCommodityNamespace(String commodityNamespace) {
    this.commodityNamespace = commodityNamespace;
  }

  public String getCommodityId() {
    return commodityId;
  }

  public void setCommodityId(String commodityId) {
    this.commodityId = commodityId;
  }

  public void setCommodityUID(String mCommodityUID) {
    this.commodityGuid = mCommodityUID;
  }

  public String getCurrencyUID() {
    return currencyGuid;
  }

  public void setCurrencyUID(String currencyUID) {
    this.currencyGuid = currencyUID;
  }

  public String getCurrencyNamespace() {
    return currencyNamespace;
  }

  public void setCurrencyNamespace(String currencyNamespace) {
    this.currencyNamespace = currencyNamespace;
  }

  public String getCurrencyId() {
    return currencyId;
  }

  public void setCurrencyId(String currencyId) {
    this.currencyId = currencyId;
  }

  public Timestamp getDate() {
    return mDate;
  }

  public void setDate(Timestamp date) {
    this.mDate = date;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  /*  public long getValueNum() {
    reduce();
    return numerator;
  }

  public void setValueNum(long valueNum) {
    this.numerator = valueNum;
  }

  public long getValueDenom() {
    reduce();
    return denominator;
  }

  public void setValueDenom(long valueDenom) {
    this.denominator = valueDenom;
  }*/

  /*  private void reduce() {
    if (denominator < 0) {
      denominator = -denominator;
      numerator = -numerator;
    }
    if (denominator != 0 && numerator != 0) {
      long num1 = numerator;
      if (num1 < 0) {
        num1 = -num1;
      }
      long num2 = denominator;
      long commonDivisor = 1;
      for (; ; ) {
        long r = num1 % num2;
        if (r == 0) {
          commonDivisor = num2;
          break;
        }
        num1 = r;
        r = num2 % num1;
        if (r == 0) {
          commonDivisor = num1;
          break;
        }
        num2 = r;
      }
      numerator /= commonDivisor;
      denominator /= commonDivisor;
    }
  }*/

  /**
   * Returns the exchange rate as a string formatted with the default locale.
   *
   * <p>It will have up to 6 decimal places.
   *
   * <p>Example: "0.123456"
   */
  /*    @Override
  public String toString() {
      BigDecimal numerator = new BigDecimal(mValueNum);
      BigDecimal denominator = new BigDecimal(mValueDenom);
      DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance();
      formatter.setMaximumFractionDigits(6);
      return formatter.format(numerator.divide(denominator, MathContext.DECIMAL32));
  }*/

  @Override
  public String toString() {
    return "GncPrice{"
        + "guid='"
        + guid
        + '\''
        + ", mCommodityUID='"
        + commodityGuid
        + '\''
        + ", commodityNamespace='"
        + commodityNamespace
        + '\''
        + ", commodityId='"
        + commodityId
        + '\''
        + ", mCurrencyUID='"
        + currencyGuid
        + '\''
        + ", currencyNamespace='"
        + currencyNamespace
        + '\''
        + ", currencyId='"
        + currencyId
        + '\''
        + ", mDate="
        + mDate
        + ", mSource='"
        + source
        + '\''
        + ", mType='"
        + type
        + '\''
        + '}';
  }
}
