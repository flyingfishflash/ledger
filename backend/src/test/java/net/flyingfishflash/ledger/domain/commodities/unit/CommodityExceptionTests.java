package net.flyingfishflash.ledger.domain.commodities.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.exceptions.AbstractApiException;
import net.flyingfishflash.ledger.domain.commodities.exceptions.CommodityCreateException;
import net.flyingfishflash.ledger.domain.commodities.exceptions.CommodityNotFoundException;

class CommodityExceptionTests {

  private static class TestCommodityException extends AbstractApiException {

    private TestCommodityException() {
      super(HttpStatus.INTERNAL_SERVER_ERROR, "Test Commodity Exception Title", "Test Detail");
    }
  }

  @Test
  void commodityException_getHttpStatus_isEqualTo500() {
    var testCommodityException = new TestCommodityException();
    assertThat(testCommodityException.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void commodityNotFoundExceptionWithStringParam_getHttpStatus_isEqualTo404() {
    var commodityException =
        new CommodityNotFoundException("This is a Commodity Not Found Exception");
    assertThat(commodityException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityNotFoundExceptionWithStringAndLongParams_getHttpStatus_isEqualTo404() {
    var commodityException = new CommodityNotFoundException(1L);
    assertThat(commodityException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityNotFoundExceptionWithTwoStringParams_getHttpStatus_isEqualTo404() {
    var commodityException = new CommodityNotFoundException("Any Guid");
    assertThat(commodityException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void commodityCreateExceptionWithStringAndExceptionParams_getHttpStatus_isEqualTo400() {
    var commodityException = new CommodityCreateException("Message", new Exception());
    assertThat(commodityException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void commodityCreateExceptionWithStringParam_getHttpStatus_isEqualTo400() {
    var commodityException = new CommodityCreateException("Message");
    assertThat(commodityException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
