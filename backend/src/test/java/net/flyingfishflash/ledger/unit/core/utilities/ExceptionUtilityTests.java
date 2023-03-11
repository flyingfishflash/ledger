package net.flyingfishflash.ledger.unit.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.exceptions.ApiException;
import net.flyingfishflash.ledger.core.utilities.ExceptionUtility;
import net.flyingfishflash.ledger.core.utilities.ProblemDetailUtility;

/** Unit tests for {@link ExceptionUtility} */
@DisplayName("ExceptionUtility")
class ExceptionUtilityTests {

  private static final Exception rootException = new Exception("Root Exception");
  private static final Exception secondException = new Exception("Second Exception", rootException);
  private static final Exception thirdException = new Exception("Third Exception", secondException);

  private static final ApiException apiExceptionWithCause =
      new ApiException(
          HttpStatus.NO_CONTENT,
          "Lorem Ipsum With Cause",
          new IllegalArgumentException("Illegal Argument Exception"));

  @Test
  void cannotBeInstantiated() {
    assertThatThrownBy(ExceptionUtility::new)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  @Test
  void constructorArgumentsMustNotBeNull() {
    assertThatThrownBy(() -> ExceptionUtility.exceptionCause(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Messages.Error.NULL_CONSTRUCTOR_ARG.value());
  }

  @Test
  void exceptionCauseReturnsLowestExceptionInTheStack() {
    var rootCauseOfThirdException = ExceptionUtility.exceptionCause(thirdException);
    assertThat(rootCauseOfThirdException).isEqualTo(rootException);
  }

  @Test
  void extractCausesReturnsEmptyArrayListForExceptionWithNoCause() {
    var extractedCauses = ExceptionUtility.extractCauses(rootException);
    assertThat(extractedCauses).isEqualTo(new ArrayList<>());
  }

  @Test
  void extractCausesReturnsArrayListOfSingleExceptionCauseDetail() {
    var extractedCauses = ExceptionUtility.extractCauses(thirdException);
    assertThat(extractedCauses)
        .containsExactly(
            new ProblemDetailUtility.ExceptionCauseDetail(
                rootException.getClass().getSimpleName(), rootException.getLocalizedMessage()));
  }

  @Test
  void extractCausesReturnsArrayListOfSingleExceptionCauseDetailWhenNoRootCause() {
    var extractedCauses = ExceptionUtility.extractCauses(apiExceptionWithCause);
    assertThat(extractedCauses)
        .containsExactly(
            new ProblemDetailUtility.ExceptionCauseDetail(
                "IllegalArgumentException", "Illegal Argument Exception"));
  }
}
