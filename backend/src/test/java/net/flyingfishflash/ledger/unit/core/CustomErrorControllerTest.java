package net.flyingfishflash.ledger.unit.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.RequestDispatcher;

import net.flyingfishflash.ledger.core.CustomErrorController;
import net.flyingfishflash.ledger.core.response.structure.Response;

/** Units tests for {@link net.flyingfishflash.ledger.core.CustomErrorController} */
@DisplayName("CustomErrorController")
class CustomErrorControllerTest {

  private MockHttpServletRequest httpServletRequest = null;

  @BeforeEach
  void setup() {
    // in practice the request uri would be /error
    httpServletRequest = new MockHttpServletRequest("Lorem Ipsum", "lorem/ipsum");
  }

  @Test
  void errorResponseIsValidWhenRequestDispatcherErrorStatusCodeIsNoContent() {
    var controller = new CustomErrorController(new DefaultErrorAttributes(), new ErrorProperties());
    var httpStatus = HttpStatus.NO_CONTENT;
    httpServletRequest.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, httpStatus.value());
    var customError = controller.error(httpServletRequest);
    assertThat(customError).isExactlyInstanceOf(ResponseEntity.class);
    assertThat(customError.getStatusCode()).isEqualTo(httpStatus);
    assertThat(customError.getBody()).isNull();
  }

  @Test
  void errorResponseIsValidWhenRequestDispatcherErrorExceptionIsSet() {
    var controller = new CustomErrorController(new DefaultErrorAttributes(), new ErrorProperties());
    var httpStatus = HttpStatus.I_AM_A_TEAPOT;
    httpServletRequest.setAttribute(RequestDispatcher.ERROR_EXCEPTION, new Exception("LoremIpsum"));
    httpServletRequest.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, httpStatus.value());
    var customError = controller.error(httpServletRequest);
    assertThat(customError.getBody()).isNotNull().isExactlyInstanceOf(Response.class);
    assertThat(customError.getStatusCode()).isEqualTo(httpStatus);
  }

  @Test
  void getErrorAttributeOptionsReturnsConfiguredErrorAttributeOptionsAlways() {
    var errorProperties = new ErrorProperties();
    errorProperties.setIncludeException(true);
    errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ALWAYS);
    errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
    errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.ALWAYS);
    var controller = new CustomErrorController(new DefaultErrorAttributes(), errorProperties);
    var attributeOptions = controller.getErrorAttributeOptions(httpServletRequest);
    assertThat(attributeOptions).isNotNull().hasOnlyFields("includes");
    assertThat(attributeOptions.getIncludes())
        .containsOnly(
            ErrorAttributeOptions.Include.EXCEPTION,
            ErrorAttributeOptions.Include.BINDING_ERRORS,
            ErrorAttributeOptions.Include.MESSAGE,
            ErrorAttributeOptions.Include.STACK_TRACE);
  }

  @Test
  void getErrorAttributeOptionsReturnsConfiguredErrorAttributeOptionsOnParam() {
    var errorProperties = new ErrorProperties();
    errorProperties.setIncludeException(true);
    errorProperties.setIncludeBindingErrors(ErrorProperties.IncludeAttribute.ON_PARAM);
    errorProperties.setIncludeMessage(ErrorProperties.IncludeAttribute.ON_PARAM);
    errorProperties.setIncludeStacktrace(ErrorProperties.IncludeAttribute.ON_PARAM);
    httpServletRequest.setParameter("errors", String.valueOf(true));
    httpServletRequest.setParameter("message", String.valueOf(true));
    httpServletRequest.setParameter("trace", String.valueOf(true));
    var controller = new CustomErrorController(new DefaultErrorAttributes(), errorProperties);
    var attributeOptions = controller.getErrorAttributeOptions(httpServletRequest);
    assertThat(attributeOptions).isNotNull().hasOnlyFields("includes");
    assertThat(attributeOptions.getIncludes())
        .containsOnly(
            ErrorAttributeOptions.Include.EXCEPTION,
            ErrorAttributeOptions.Include.BINDING_ERRORS,
            ErrorAttributeOptions.Include.MESSAGE,
            ErrorAttributeOptions.Include.STACK_TRACE);
  }
}
