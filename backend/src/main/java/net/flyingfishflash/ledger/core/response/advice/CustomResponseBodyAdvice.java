package net.flyingfishflash.ledger.core.response.advice;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import net.flyingfishflash.ledger.core.response.structure.*;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  private static final Logger logger = LoggerFactory.getLogger(CustomResponseBodyAdvice.class);

  @Autowired private ObjectMapper objectMapper;

  @Override
  public boolean supports(
      @NonNull MethodParameter returnType,
      @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

    // exclude springdoc/swagger from beforeBodyWrite
    return Optional.ofNullable(returnType.getMethod())
        .map(method -> !method.getName().equals("openapiJson"))
        .orElse(true);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Object beforeBodyWrite(
      Object o,
      MethodParameter methodParameter,
      @NonNull MediaType mediaType,
      @NonNull Class aClass,
      @NonNull ServerHttpRequest serverHttpRequest,
      @NonNull ServerHttpResponse serverHttpResponse) {

    var method = methodParameter.getMethod();
    ApplicationResponse response = null;

    // pristine
    if (o instanceof ApplicationResponse applicationResponse) {
      response = applicationResponse;
    }

    // known error
    else if (o instanceof Throwable throwable && !(o instanceof ErrorResponseException)) {
      var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, throwable);
      response =
          new Response<>(
              problemDetail,
              "Problem found in CustomResponseBodyAdvice",
              serverHttpRequest.getMethod().toString());

    }

    // known error
    else if (o instanceof ErrorResponseException errorResponseException) {
      var pd = errorResponseException.getBody();
      ProblemDetailUtility.setCustomPropertiesFromThrowable(pd, errorResponseException);
      response =
          new Response<>(
              pd,
              "CustomResponseBodyAdvice <- ProblemDetail <- ErrorResponseException",
              serverHttpRequest.getMethod().toString());
    }

    // known error
    else if (o instanceof ProblemDetail) {
      response =
          new Response<>(
              o,
              "CustomResponseBodyAdvice <- ProblemDetail",
              serverHttpRequest.getMethod().toString());
    }

    // success
    else if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)
        && (method != null)
        && !method.isAnnotationPresent(IgnoreResponseBinding.class)) {

      logger.warn("Object wrapped in Response with successful disposition by default {}", o);
      response =
          new Response<>(
              o,
              "Object wrapped in Response with successful disposition by default",
              serverHttpRequest.getMethod().toString(),
              serverHttpRequest.getURI());
    }

    if (response != null) {
      if (logger.isInfoEnabled()) {
        try {
          logger.info(objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
          logger.error("JsonProcessingException while converting response object to json");
          logger.info("response object: {}", response);
        }
      }
      return response;
    } else {
      logger.warn(
          "Returning object from CustomResponseBodyAdvice.beforeBodyWrite() without examination: {}",
          o);
      return o;
    }
  }
}
