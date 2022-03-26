package net.flyingfishflash.ledger.commodities.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.commodities.exceptions.CommodityCreateException;
import net.flyingfishflash.ledger.commodities.exceptions.CommodityNotFoundException;
import net.flyingfishflash.ledger.commodities.exceptions.GeneralCommodityException;

public class CommodityExceptionTests {

  private static class TestCommodityException extends GeneralCommodityException {

    private TestCommodityException() {
      super("Test Commodity Exception");
    }
  }

  @Test
  public void testCommodityException_getHttpStatus() {
    TestCommodityException testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void testCommodityException_getErrorDomain() {
    TestCommodityException testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getErrorDomain()).isEqualTo("Commodities");
  }

  @Test
  public void testCommodityException_getErrorSubject() {
    TestCommodityException testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getErrorSubject()).isEqualTo("Commodity");
  }

  @Test
  public void testCommodityNotFoundException_getHttpStatus1() {
    CommodityNotFoundException commodityException =
        new CommodityNotFoundException("This is a Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCommodityNotFoundException_getHttpStatus2() {
    CommodityNotFoundException commodityException =
        new CommodityNotFoundException(1L, "Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCommodityNotFoundException_getHttpStatus3() {
    CommodityNotFoundException commodityException =
        new CommodityNotFoundException("Any Guid", "Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCommodityCreateException_getHttpStatus1() {
    CommodityCreateException commodityException =
        new CommodityCreateException("Message", new Exception());
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testCommodityCreateException_getHttpStatus2() {
    CommodityCreateException commodityException = new CommodityCreateException("Message");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
