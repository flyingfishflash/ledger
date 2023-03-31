package net.flyingfishflash.ledger.unit.core.response.advice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willThrow;

import java.net.URI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RestController;

import net.flyingfishflash.ledger.core.response.advice.CustomResponseBodyAdvice;
import net.flyingfishflash.ledger.core.response.structure.*;

/**
 * Unit tests for {@link CustomResponseBodyAdvice}
 *
 * <p>Note: Console output is 'null' due to mocked ObjectMapper
 */
@DisplayName("CustomResponseBodyAdvice")
@ExtendWith(MockitoExtension.class)
class CustomResponseBodyAdviceTests {

  private static MethodParameter mpTestControllerGetStringWithResponseBinding;
  private static MethodParameter mpTestControllerGetStringIgnoreResponseBinding;
  private static MethodParameter mpTestControllerNoAnnotationGetStringWithResponseBinding;
  private static MethodParameter mpTestControllerNoAnnotationGetStringIgnoreResponseBinding;

  @Mock ObjectMapper objectMapper;
  @InjectMocks CustomResponseBodyAdvice responseBodyAdvice;

  @BeforeAll
  static void setup() throws NoSuchMethodException {

    mpTestControllerGetStringWithResponseBinding =
        new MethodParameter(
            TestController.class.getDeclaredMethod("getStringWithResponseBinding"), -1);

    mpTestControllerGetStringIgnoreResponseBinding =
        new MethodParameter(
            TestController.class.getDeclaredMethod("getStringIgnoreResponseBinding"), -1);

    mpTestControllerNoAnnotationGetStringWithResponseBinding =
        new MethodParameter(
            TestControllerNoAnnotation.class.getDeclaredMethod("getStringWithResponseBinding"), -1);

    mpTestControllerNoAnnotationGetStringIgnoreResponseBinding =
        new MethodParameter(
            TestControllerNoAnnotation.class.getDeclaredMethod("getStringIgnoreResponseBinding"),
            -1);
  }

  @Test
  void supportsReturnsFalseWhenMethodIsOpenapiJson() throws NoSuchMethodException {

    assertThat(
            responseBodyAdvice.supports(
                new MethodParameter(TestClass.class.getDeclaredMethod("openapiJson"), -1),
                ByteArrayHttpMessageConverter.class))
        .isFalse();

    assertThat(
            responseBodyAdvice.supports(
                new MethodParameter(TestClass.class.getDeclaredMethod("notOpenapiJson"), -1),
                ByteArrayHttpMessageConverter.class))
        .isTrue();
  }

  private Object callBeforeBodyWrite(Object o, MethodParameter methodParameter) {

    MockHttpServletRequest mockRequest = new MockHttpServletRequest("Lorem Ipsum", "/lorem/ipsum");
    ServerHttpRequest request = new ServletServerHttpRequest(mockRequest);
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    ServerHttpResponse response = new ServletServerHttpResponse(mockResponse);

    return responseBodyAdvice.beforeBodyWrite(
        o, methodParameter, MediaType.ALL, Object.class, request, response);
  }

  /** 'o' is returned immediately in pristine condition because it's type ApplicationResponse */
  @Test
  void beforeBodyWriteReturnsPristineApplicationResponse() {
    Object beforeBodyWrite;
    Object o;

    o = new Response<>("Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum", URI.create("/lorem/ipsum"));
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isEqualTo(o);
  }

  @Test
  void beforeBodyWriteReturnsPristineApplicationResponseWithJsonProcessingException()
      throws JsonProcessingException {
    Object beforeBodyWrite;
    Object o;
    o = new Response<>("Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum", URI.create("/lorem/ipsum"));
    willThrow(JsonProcessingException.class).given(objectMapper).writeValueAsString(o);
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isEqualTo(o);
  }

  /** 'o' is returned repackaged as content in a Response */
  @Test
  void beforeBodyWriteReturnsResponseForKnownErrorTypes() {
    Object beforeBodyWrite;
    Object o;

    /* Test Class with @RestController annotation */

    // 'o' is returned as type ProblemDetail repackaged as content in a Response
    o = new Exception("Lorem Ipsum");
    beforeBodyWrite = callBeforeBodyWrite(o, mpTestControllerGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);

    // 'o' is returned as type ProblemDetail repackaged as content in a Response
    o = new ErrorResponseException(HttpStatus.I_AM_A_TEAPOT);
    beforeBodyWrite = callBeforeBodyWrite(o, mpTestControllerGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);

    // 'o' is returned in pristine condition, repackaged as content in a Response
    o = ProblemDetail.forStatus(418);
    beforeBodyWrite = callBeforeBodyWrite(o, mpTestControllerGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);

    /* Test Class without @RestController annotation */

    // 'o' is returned as type ProblemDetail repackaged as content in a Response
    o = new Exception("Lorem Ipsum");
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);

    // 'o' is returned as type ProblemDetail repackaged as content in a Response
    o = new ErrorResponseException(HttpStatus.I_AM_A_TEAPOT);
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);

    // 'o' is returned in pristine condition, repackaged as content in a Response
    o = ProblemDetail.forStatus(418);
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isExactlyInstanceOf(Response.class);
  }

  @Test
  void beforeBodyWriteReturnsResponseWithPristineObject() {
    Object beforeBodyWrite;
    Object o;

    // @RestController annotation is present
    // @IgnoreResponseBinding annotation is not present on method
    // 'o' is neither of types Throwable, ErrorResponseException or ProblemDetail
    o = "o is returned in pristine condition, wrapped in Response()";
    beforeBodyWrite = callBeforeBodyWrite(o, mpTestControllerGetStringWithResponseBinding);
    assertThat(beforeBodyWrite)
        .isNotNull()
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(
            new Response<>(
                o,
                "Object wrapped in Response with successful disposition by default",
                "Lorem Ipsum",
                URI.create("http://localhost/lorem/ipsum")));
  }

  @Test
  void beforeBodyWriteReturnsPristineObject() {
    Object beforeBodyWrite;
    Object o = "o is returned in pristine condition, not wrapped in Response()";

    // @RestController annotation is present
    // @IgnoreResponseBinding annotation is present on method
    // 'o' is neither of types Throwable, ErrorResponseException or ProblemDetail
    beforeBodyWrite = callBeforeBodyWrite(o, mpTestControllerGetStringIgnoreResponseBinding);

    assertThat(beforeBodyWrite).isNotNull().isEqualTo(o);
    // @RestController annotation is not present
    // @IgnoreResponseBinding annotation is not present on method
    // 'o' is neither of types Throwable, ErrorResponseException or ProblemDetail
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringWithResponseBinding);

    assertThat(beforeBodyWrite).isNotNull().isEqualTo(o);
    // @RestController annotation is not present
    // @IgnoreResponseBinding annotation is present on method
    // 'o' is neither of types Throwable, ErrorResponseException or ProblemDetail
    beforeBodyWrite =
        callBeforeBodyWrite(o, mpTestControllerNoAnnotationGetStringIgnoreResponseBinding);
    assertThat(beforeBodyWrite).isNotNull().isEqualTo(o);
  }
}

@RestController
class TestController {

  String getStringWithResponseBinding() {
    return "Lorem Ipsum With Response Binding";
  }

  @IgnoreResponseBinding
  String getStringIgnoreResponseBinding() {
    return "Lorem Ipsum Ignore Response Binding";
  }
}

class TestControllerNoAnnotation {

  String getStringWithResponseBinding() {
    return "Lorem Ipsum";
  }

  @IgnoreResponseBinding
  String getStringIgnoreResponseBinding() {
    return "Lorem Ipsum";
  }
}

class TestClass {
  public String openapiJson() {
    return "Lorem Ipsum";
  }

  public String notOpenapiJson() {
    return "Lorem Ipsum";
  }
}
