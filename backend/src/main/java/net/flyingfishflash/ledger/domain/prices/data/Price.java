package net.flyingfishflash.ledger.domain.prices.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

import jakarta.persistence.*;

import net.flyingfishflash.ledger.domain.books.data.Book;
import net.flyingfishflash.ledger.domain.commodities.data.Commodity;

@Entity
@Table(name = "price")
public class Price {

  @Id
  @SequenceGenerator(name = "price_id_seq", sequenceName = "price_seq", allocationSize = 1)
  @GeneratedValue(generator = "price_id_seq")
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Book book;

  @Column(name = "guid", unique = true, updatable = false, length = 32)
  private String guid;

  @ManyToOne
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  /*
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Commodity currency;
  */
  @Column(name = "currency", length = 3)
  private String currency;

  @Column(name = "date")
  private Timestamp date;

  @Column(name = "source")
  private String source;

  @Column(name = "type")
  private String type;

  @Column(name = "numerator")
  private Long numerator;

  @Column(name = "denominator")
  private Long denominator;

  @Column(name = "price", precision = 38, scale = 18)
  private BigDecimal price;

  public Price() {}

  public Price(String guid) {
    this.guid = guid;
  }

  public Price(Book book) {
    this.book = book;
  }

  public Price(String guid, Book book) {
    this.guid = guid;
    this.book = book;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public Commodity getCommodity() {
    return commodity;
  }

  public void setCommodity(Commodity commodity) {
    this.commodity = commodity;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
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

  /*
  public Long getNumerator() {
    return numerator;
  }

  public void setNumerator(Long numerator) {
    this.numerator = numerator;
  }

  public Long getDenominator() {
    return denominator;
  }

  public void setDenominator(Long denominator) {
    this.denominator = denominator;
  }*/

  public void setFraction(Long numerator, Long denominator) {

    this.numerator = numerator;
    this.denominator = denominator;
    /* Update price */
    var n = new BigDecimal(numerator);
    var d = new BigDecimal(denominator);
    var scale = Integer.numberOfTrailingZeros(this.getCommodity().getFraction());
    this.price = n.divide(d, scale, RoundingMode.HALF_EVEN);
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
