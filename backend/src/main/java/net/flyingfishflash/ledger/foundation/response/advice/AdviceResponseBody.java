package net.flyingfishflash.ledger.foundation.response.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import net.flyingfishflash.ledger.foundation.response.structure.IgnoreResponseBinding;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.success.SuccessResponse;

@RestControllerAdvice(basePackages = {"net.flyingfishflash.ledger"})
public class AdviceResponseBody implements ResponseBodyAdvice<Object> {

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
        && !(method.isAnnotationPresent(IgnoreResponseBinding.class))
        && !(o instanceof ErrorResponse)
        && !(o instanceof SuccessResponse)) {

      return new SuccessResponse<>(o);
    }

    return o;
  }
}
