package net.flyingfishflash.ledger.core;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomErrorController extends BasicErrorController {

  public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
    super(errorAttributes, serverProperties.getError());
  }

  @Override
  @RequestMapping
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
      return new ResponseEntity<>(status);
    }

    Map<String, Object> body = new LinkedHashMap<>();
    var errorAttributes =
        getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));

    var problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setInstance(URI.create((String) errorAttributes.get("path")));
    var message = (String) errorAttributes.get("message");
    if (!StringUtils.hasText(message)) {
      problemDetail.setDetail(String.format("%s", status.getReasonPhrase()));
    } else {
      if (status == HttpStatus.NOT_FOUND) {
        problemDetail.setTitle("URI Not Found");
        problemDetail.setDetail(
            String.format("%s: %s", problemDetail.getTitle(), problemDetail.getInstance()));
      } else {
        problemDetail.setDetail((String) errorAttributes.get("message"));
      }
    }
    body.put("errorAttributes", errorAttributes);
    body.put("problemDetail", problemDetail);

    return new ResponseEntity<>(body, status);
  }
}
