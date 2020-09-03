package net.flyingfishflash.ledger.foundation.response.advice;

import net.flyingfishflash.ledger.foundation.response.structure.IgnoreResponseBinding;
import net.flyingfishflash.ledger.foundation.response.structure.errors.ErrorResponse;
import net.flyingfishflash.ledger.foundation.response.structure.success.SuccessResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

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
    if (methodParameter.getContainingClass().isAnnotationPresent(RestController.class)) {

      if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseBinding.class) == false) {
        if ((!(o instanceof ErrorResponse)) && (!(o instanceof SuccessResponse))) {
          SuccessResponse<Object> responseBody = new SuccessResponse<>(o);
          return responseBody;
        }
      }
    }
    return o;
  }
}
