package net.flyingfishflash.ledger.core.response.advice;

import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import net.flyingfishflash.ledger.core.response.structure.ApplicationErrorResponse;
import net.flyingfishflash.ledger.core.response.structure.ApplicationSuccessResponse;
import net.flyingfishflash.ledger.core.response.structure.IgnoreResponseBinding;

@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object o,
      MethodParameter methodParameter,
      MediaType mediaType,
      Class aClass,
      ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {

    var method = methodParameter.getMethod();

    if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)
        && (method != null)
        && !method.isAnnotationPresent(IgnoreResponseBinding.class)
        && !(o instanceof ApplicationSuccessResponse)
        && !(o instanceof ApplicationErrorResponse)
        && !(o instanceof ProblemDetail)
        && !(o instanceof Exception)) {
      return new ApplicationSuccessResponse<>(o);
    } else if (o instanceof Exception exception && !(o instanceof ErrorResponseException)) {
      return new ApplicationErrorResponse(exception);
      // Map should be coming from CustomErrorController, where a ProblemDetail entry was created
    } else if (o instanceof Map<?, ?> hashmap) {
      if (hashmap.containsKey("problemDetail")) {
        return new ApplicationErrorResponse((ProblemDetail) hashmap.get("problemDetail"));
      } else return o;
    } else return o;
  }
}
