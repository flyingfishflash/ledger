package net.flyingfishflash.ledger.core.response.structure;

import java.net.URI;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.utilities.IdentifierUtility;

/**
 * @param <T> Type of the <i>content</i> field of the Response
 */
@JsonPropertyOrder({"id", "disposition", "instance", "method"})
public class Response<T> implements ApplicationResponse<T> {

  private final String id = IdentifierUtility.identifier();
  private final Disposition disposition;
  private final String method;
  private final String message;
  private final int size;
  private final T content;
  private final URI instance;

  /**
   * @param content
   * @param message
   * @param method
   * @param instance
   */
  public Response(T content, String message, String method, URI instance) {
    if (content == null || instance == null || method == null || message == null) {
      throw new IllegalArgumentException(Messages.Error.NULL_CONSTRUCTOR_ARG.value());
    } else if (content instanceof ProblemDetail) {
      throw new IllegalArgumentException("Content must not be of type ProblemDetail");
    }

    this.disposition = calcDisposition(HttpStatus.OK.value());
    this.message = message;
    this.content = content;
    this.size = calcSize();
    this.method = method;
    this.instance = instance;
  }

  /**
   * @param content
   * @param message
   * @param method
   */
  public Response(T content, String message, String method) {
    if (content == null || message == null || method == null) {
      throw new IllegalArgumentException(Messages.Error.NULL_CONSTRUCTOR_ARG.value());
    } else if (content instanceof ProblemDetail problemDetail) {
      this.disposition = calcDisposition(problemDetail.getStatus());
      this.message = message;
      this.content = content;
      this.size = calcSize();
      this.method = method;
      this.instance = problemDetail.getInstance();
    } else {
      throw new IllegalArgumentException("Content must be of type ProblemDetail");
    }
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Disposition getDisposition() {
    return disposition;
  }

  public String getMethod() {
    return method.toLowerCase();
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public T getContent() {
    return content;
  }

  @Override
  public String getInstance() {
    return instance.getPath();
  }

  /** Calculate the number of items included in the content */
  private int calcSize() {
    var s = 1;
    if (this.content instanceof List<?> bodyList) {
      s = bodyList.size();
    }
    if (this.content instanceof Map<?, ?> bodyMap) {
      s = bodyMap.size();
    }
    return s;
  }

  /** Calculate the disposition of the Api Event from the Http status */
  private Disposition calcDisposition(int httpStatus) {
    var d = Disposition.SUCCESS;
    if (HttpStatus.valueOf(httpStatus).is4xxClientError()) {
      d = Disposition.FAILURE;
    } else if (HttpStatus.valueOf(httpStatus).is5xxServerError()) {
      d = Disposition.ERROR;
    }
    return d;
  }

  @Override
  public String toString() {
    return "Response{"
        + "id='"
        + id
        + '\''
        + ", disposition="
        + disposition
        + ", method='"
        + method
        + '\''
        + ", message='"
        + message
        + '\''
        + ", size="
        + size
        + ", content="
        + content
        + ", instance="
        + instance
        + '}';
  }
}
