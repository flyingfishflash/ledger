package net.flyingfishflash.ledger.unit.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.net.URI;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.ProblemDetail;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

/** Unit tests for {@link net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility} */
@DisplayName("ProblemDetailUtility")
class ProblemDetailUtilityTests {

  @Test
  void utilityClassCannotBeInstantiated() {
    assertThatThrownBy(ProblemDetailUtility::new)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  @Nested
  class SetCustomPropertiesFromThrowable {

    @Test
    void methodArgumentsMustNotBeNull() {
      assertThatThrownBy(
              () ->
                  ProblemDetailUtility.setCustomPropertiesFromThrowable(
                      null, new Exception("Lorem Ipsum")))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());

      assertThatThrownBy(
              () ->
                  ProblemDetailUtility.setCustomPropertiesFromThrowable(
                      ProblemDetail.forStatus(418), null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());
    }

    @Test
    void eachThrowableSourcedCustomPropertyIsPresentAndNotNull() {

      var problemDetail = ProblemDetail.forStatus(418);
      var exception = new IllegalStateException("Lorem Ipsum", new NumberFormatException());
      ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

      assertThat(problemDetail.getProperties())
          .hasSize(2)
          .hasFieldOrProperty(ProblemDetailUtility.CustomProperty.EXCEPTION.key())
          .isNotNull()
          .hasFieldOrProperty(ProblemDetailUtility.CustomProperty.CAUSES.key())
          .isNotNull();
    }

    @Test
    void exceptionCustomPropertyOfEachProblemDetailContainsTheExceptionClassSimpleName() {
      var problemDetail = ProblemDetail.forStatus(418);
      var exception = new IllegalStateException("Lorem Ipsum");
      ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

      assertThat(problemDetail.getProperties())
          .containsEntry(
              ProblemDetailUtility.CustomProperty.EXCEPTION.key(),
              exception.getClass().getSimpleName());
    }
  }

  @Nested
  class SetCustomProperties {

    @Test
    void methodArgumentsMustNotBeNull() {
      assertThatThrownBy(
              () ->
                  ProblemDetailUtility.setCustomProperties(
                      null, new Exception("Lorem Ipsum"), new LinkedHashMap<>()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());

      assertThatThrownBy(
              () ->
                  ProblemDetailUtility.setCustomProperties(
                      ProblemDetail.forStatus(418), null, new LinkedHashMap<>()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());

      assertThatThrownBy(
              () ->
                  ProblemDetailUtility.setCustomProperties(
                      ProblemDetail.forStatus(418), new Exception("Lorem Ipsum"), null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());
    }

    @ParameterizedTest
    @EnumSource(ProblemDetailUtility.CustomProperty.class)
    void eachCustomPropertyDetailPropertyIsPresentAndNotNull(
        ProblemDetailUtility.CustomProperty customProperty) {

      var problemDetail = ProblemDetail.forStatus(418);
      var exception = new IllegalStateException("Lorem Ipsum");
      var errorAttributes = new LinkedHashMap<String, Object>();
      errorAttributes.put(ProblemDetailUtility.ErrorAttribute.TRACE.key(), "Lorem Ipsum");
      errorAttributes.put(ProblemDetailUtility.ErrorAttribute.ERRORS.key(), "Lorem Ipsum");
      ProblemDetailUtility.setCustomProperties(problemDetail, exception, errorAttributes);

      assertThat(problemDetail.getProperties())
          .hasSize(4)
          .hasFieldOrProperty(customProperty.key())
          .isNotNull();
    }

    @Test
    void exceptionCustomPropertyOfEachProblemDetailContainsTheExceptionClassSimpleName() {
      var problemDetail = ProblemDetail.forStatus(418);
      var exception = new IllegalStateException("Lorem Ipsum");
      ProblemDetailUtility.setCustomPropertiesFromThrowable(problemDetail, exception);

      assertThat(problemDetail.getProperties())
          .containsEntry(
              ProblemDetailUtility.CustomProperty.EXCEPTION.key(),
              exception.getClass().getSimpleName());
    }
  }

  @Nested
  class CreateProblemDetail {

    @Test
    void methodArgumentsMustNotBeNull() {
      assertThatThrownBy(
              () -> ProblemDetailUtility.createProblemDetail(null, new LinkedHashMap<>()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());

      assertThatThrownBy(
              () -> ProblemDetailUtility.createProblemDetail(new Exception("Lorem Ipsum"), null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage(Messages.Error.NULL_METHOD_ARG.value());
    }

    @Test
    void problemDetailStatusIs500WhenAttributeStatusIsNull() {
      var attributes = new LinkedHashMap<String, Object>();
      attributes.put(ProblemDetailUtility.ErrorAttribute.STATUS.key(), null);
      var problemDetail =
          ProblemDetailUtility.createProblemDetail(new Exception("Lorem Ipsum"), attributes);
      assertThat(problemDetail.getStatus()).isEqualTo(500);
    }

    @Test
    void problemDetailStatusIs500WhenAttributeStatusIsMissing() {
      var attributes = new LinkedHashMap<String, Object>();
      var problemDetail =
          ProblemDetailUtility.createProblemDetail(new Exception("Lorem Ipsum"), attributes);
      assertThat(problemDetail.getStatus()).isEqualTo(500);
    }

    @Test
    void problemDetailNonCustomPropertiesMappedFromAttributes() {
      var attributes = new LinkedHashMap<String, Object>();
      attributes.put(ProblemDetailUtility.ErrorAttribute.STATUS.key(), 418);
      attributes.put(ProblemDetailUtility.ErrorAttribute.ERROR.key(), "lorem ipsum error");
      attributes.put(ProblemDetailUtility.ErrorAttribute.MESSAGE.key(), "lorem ipsum message");
      attributes.put(ProblemDetailUtility.ErrorAttribute.PATH.key(), "lorem/ipsum/path");
      var problemDetail =
          ProblemDetailUtility.createProblemDetail(new Exception("Lorem Ipsum"), attributes);
      assertThat(problemDetail.getStatus()).isEqualTo(418);
      assertThat(problemDetail.getTitle()).isEqualTo("lorem ipsum error");
      assertThat(problemDetail.getDetail()).isEqualTo("lorem ipsum message");
      assertThat(problemDetail.getInstance()).isEqualTo(URI.create("lorem/ipsum/path"));
    }
  }
}
