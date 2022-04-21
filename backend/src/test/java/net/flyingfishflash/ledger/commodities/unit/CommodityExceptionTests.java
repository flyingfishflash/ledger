package net.flyingfishflash.ledger.commodities.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.commodities.exceptions.CommodityCreateException;
import net.flyingfishflash.ledger.commodities.exceptions.CommodityNotFoundException;
import net.flyingfishflash.ledger.commodities.exceptions.GeneralCommodityException;

class CommodityExceptionTests {

  private static class TestCommodityException extends GeneralCommodityException {

    private TestCommodityException() {
      super("Test Commodity Exception");
    }
  }

  @Test
  void commodityException_getErrorDomain_isEqualToCommodities() {
    var testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getErrorDomain()).isEqualTo("Commodities");
  }

  @Test
  void commodityException_getHttpStatus_isEqualTo500() {
    var testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void commodityException_getErrorSubject_isEqualToCommodity() {
    var testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getErrorSubject()).isEqualTo("Commodity");
  }

  @Test
  void commodityNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var commodityException =
        new CommodityNotFoundException("This is a Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var commodityException = new CommodityNotFoundException(1L, "Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var commodityException =
        new CommodityNotFoundException("Any Guid", "Commodity Not Found Exception");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityCreateExceptionWithStringAndExceptionParams_getHttpStatus_isEqualTo400() {
    var commodityException = new CommodityCreateException("Message", new Exception());
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void commodityCreateExceptionWithStringParam_getHttpStatus_isEqualTo400() {
    var commodityException = new CommodityCreateException("Message");
    assertThat(commodityException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
