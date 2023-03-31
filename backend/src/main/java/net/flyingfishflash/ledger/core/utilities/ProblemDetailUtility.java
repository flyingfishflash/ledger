package net.flyingfishflash.ledger.core.utilities;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;

import net.flyingfishflash.ledger.core.Messages;

public final class ProblemDetailUtility {

  private static final Logger logger = LoggerFactory.getLogger(ProblemDetailUtility.class);

  private static final String NULL_ATTRIBUTE_WARNING = "errorAttributes {} key has null value";
  private static final String MISSING_ATTRIBUTE_WARNING = "errorAttributes {} key is not present";

  public ProblemDetailUtility() {
    throw new UnsupportedOperationException(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  @SuppressWarnings("java:S1192")
  public static ProblemDetail createProblemDetail(
      Throwable throwable, Map<String, Object> errorAttributes) {

    if (errorAttributes == null || throwable == null)
      throw new IllegalArgumentException(Messages.Error.NULL_METHOD_ARG.value());

    // log warning for any missing error attributes
    Arrays.stream(ErrorAttribute.values())
        .filter(
            attribute ->
                attribute.equals(ErrorAttribute.ERROR)
                    || attribute.equals(ErrorAttribute.MESSAGE)
                    || attribute.equals(ErrorAttribute.PATH))
        .forEach(
            attribute -> {
              if (!errorAttributes.containsKey(attribute.key())) {
                logger.warn(MISSING_ATTRIBUTE_WARNING, attribute.key());
              }
            });

    // log warning for any null error attribute values
    errorAttributes.entrySet().stream()
        .filter(entry -> entry.getValue() == null)
        .forEach(
            entry -> {
              if (logger.isWarnEnabled()) logger.warn(NULL_ATTRIBUTE_WARNING, entry.getKey());
            });

    var httpStatus = Optional.ofNullable(errorAttributes.get("status")).orElse(500);
    var problemDetail = ProblemDetail.forStatus((int) httpStatus);

    // set non-custom ProblemDetail properties
    errorAttributes.entrySet().stream()
        .filter(entry -> entry.getValue() != null)
        .filter(
            entry ->
                entry.getKey().equals(ErrorAttribute.ERROR.key())
                    || entry.getKey().equals(ErrorAttribute.MESSAGE.key())
                    || entry.getKey().equals(ErrorAttribute.PATH.key()))
        .forEach(
            entry -> {
              if (entry.getKey().equals(ErrorAttribute.ERROR.key())) {
                problemDetail.setTitle(String.valueOf(entry.getValue()));
              }
              if (entry.getKey().equals(ErrorAttribute.MESSAGE.key())) {
                problemDetail.setDetail(String.valueOf(entry.getValue()));
              }
              if (entry.getKey().equals(ErrorAttribute.PATH.key())) {
                problemDetail.setInstance(URI.create(String.valueOf(entry.getValue())));
              }
            });

    setCustomProperties(problemDetail, throwable, errorAttributes);
    return problemDetail;
  }

  /**
   * Maps Exception fields to PropertyDetail custom properties
   *
   * <ul>
   *   <li>exception = Exception class simple name
   *   <li>causes = Exception cause as prepared by ExceptionUtility.extractCauses()
   * </ul>
   *
   * @param problemDetail ProblemDetail to update
   * @param throwable Exception to map into custom properties
   */
  public static void setCustomPropertiesFromThrowable(
      ProblemDetail problemDetail, Throwable throwable) {

    if (problemDetail == null || throwable == null)
      throw new IllegalArgumentException(Messages.Error.NULL_METHOD_ARG.value());

    problemDetail.setProperty(CustomProperty.EXCEPTION.key(), throwable.getClass().getSimpleName());
    problemDetail.setProperty(
        CustomProperty.CAUSES.key(), ExceptionUtility.extractCauses(throwable));
  }

  public static void setCustomProperties(
      ProblemDetail problemDetail, Throwable throwable, Map<String, Object> errorAttributes) {

    if (problemDetail == null || throwable == null || errorAttributes == null)
      throw new IllegalArgumentException(Messages.Error.NULL_METHOD_ARG.value());

    setCustomPropertiesFromThrowable(problemDetail, throwable);

    Arrays.stream(CustomProperty.values())
        .filter(CustomProperty::isMappedFromErrorAttribute)
        .forEach(
            e -> {
              var key = e.key();
              if (errorAttributes.containsKey(key) && (errorAttributes.get(key) != null)) {
                problemDetail.setProperty(key, errorAttributes.get(key));
              }
            });
  }

  /**
   * Contains a value of the <a
   * href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ProblemDetail.html">ProblemDetail</a>
   * custom property {@link ProblemDetailUtility.CustomProperty CustomProperty.CAUSES}
   */
  public record ExceptionCauseDetail(String exception, String localizedMessage) {}

  /** {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes } */
  public enum ErrorAttribute {
    /** ErrorAttribute.timestamp -> null */
    TIMESTAMP,
    /** ErrorAttribute.status -> ProblemDetail.status */
    STATUS,
    /** ErrorAttribute.error -> ProblemDetail.title */
    ERROR,
    /** ErrorAttribute.message -> ProblemDetail.detail */
    MESSAGE,
    /** ErrorAttribute.path -> ProblemDetail.instance */
    PATH,
    /** ErrorAttribute.exception -> null */
    EXCEPTION,
    /** ErrorAttribute.errors -> ProblemDetail custom property */
    ERRORS,
    /** ErrorAttribute.trace -> ProblemDetail custom property */
    TRACE;

    /**
     * Returns an enum constant name() formatted in Camel Case (dromedaryCamelCase)
     *
     * @return An enum constant name() formatted in Camel Case (dromedaryCamelCase)
     */
    public String key() {
      return name().contains("_")
          ? StringUtility.fromSnakeCaseToCamelCase(name())
          : name().toLowerCase();
    }
  }

  public enum CustomProperty {
    /** Exception class simple name */
    EXCEPTION,
    /**
     * Exception cause as determined by {@link
     * net.flyingfishflash.ledger.core.utilities.ExceptionUtility }
     */
    CAUSES,
    /** Mapped from ErrorAttribute.trace */
    TRACE(true),
    /** Mapped form ErrorAttribute.trace, Contains binding errors */
    ERRORS(true);

    private final Boolean isMappedFromErrorAttribute;

    CustomProperty() {
      this.isMappedFromErrorAttribute = false;
    }

    CustomProperty(Boolean isMappedFromErrorAttribute) {
      this.isMappedFromErrorAttribute = isMappedFromErrorAttribute;
    }

    /** Indicator of whether property value is mapped from a corresponding ErrorAttribute */
    public Boolean isMappedFromErrorAttribute() {
      return isMappedFromErrorAttribute;
    }

    /**
     * Returns an enum constant name() formatted in Camel Case (dromedaryCamelCase)
     *
     * @return An enum constant name() formatted in Camel Case (dromedaryCamelCase)
     */
    public String key() {
      return name().contains("_")
          ? StringUtility.fromSnakeCaseToCamelCase(name())
          : name().toLowerCase();
    }
  }
}
