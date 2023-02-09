package net.flyingfishflash.ledger.foundation.response.structure;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("java:S1948")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationSuccessResponse<T> {

  private static class SuccessResponseStructure<T> implements java.io.Serializable {
    private T body;
    private int length = 1;
    private String message;

    public SuccessResponseStructure(T body, int length, String message) {
      this.body = body;
      this.length = length;
      this.message = message;
      if (length == 0) {
        if (this.body instanceof List<?> bodyList) {
          this.length = bodyList.size();
        }

        if (this.body instanceof Map<?, ?> bodyMap) {
          this.length = bodyMap.size();
        }
      }
    }

    public SuccessResponseStructure(T body, String message) {
      this.body = body;
      this.message = message;

      if (this.body instanceof List<?> bodyList) {
        this.length = bodyList.size();
      }

      if (this.body instanceof Map<?, ?> bodyMap) {
        this.length = bodyMap.size();
      }
    }

    public SuccessResponseStructure(T body, Integer length) {
      this.body = body;
      this.length = length;
    }

    public SuccessResponseStructure(T body) {
      this.body = body;
      if (this.body instanceof List<?> bodyList) {
        this.length = bodyList.size();
      }

      if (this.body instanceof Map<?, ?> bodyMap) {
        this.length = bodyMap.size();
      }
    }

    public T getBody() {
      return body;
    }

    public void setBody(T body) {
      this.body = body;
    }

    public int getLength() {
      return length;
    }

    public void setLength(Integer length) {
      this.length = length;
    }

    public void setLength(int length) {
      this.length = length;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

  private final String status;
  private SuccessResponseStructure<T> response;

  public ApplicationSuccessResponse(T object) {
    this.response = new SuccessResponseStructure<>(object);
    this.status = ApiStatus.SUCCESS.name().toLowerCase();
  }

  public ApplicationSuccessResponse(T object, String message) {
    this.response = new SuccessResponseStructure<>(object, message);
    this.status = ApiStatus.SUCCESS.name().toLowerCase();
  }

  public ApplicationSuccessResponse(T object, Integer length, String message) {
    this.response = new SuccessResponseStructure<>(object, length, message);
    this.status = ApiStatus.SUCCESS.name().toLowerCase();
  }

  public ApplicationSuccessResponse(T object, Integer length) {
    this.response = new SuccessResponseStructure<>(object, length);
    this.status = ApiStatus.SUCCESS.name().toLowerCase();
  }

  public SuccessResponseStructure<T> getResponse() {
    return response;
  }

  public void setResponse(SuccessResponseStructure<T> response) {
    this.response = response;
  }

  public String getStatus() {
    return status;
  }
}
