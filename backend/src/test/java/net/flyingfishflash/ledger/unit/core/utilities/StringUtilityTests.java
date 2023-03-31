package net.flyingfishflash.ledger.unit.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.utilities.StringUtility;

/** Unit tests for {@link net.flyingfishflash.ledger.core.utilities.StringUtility} */
@DisplayName("StringUtility")
class StringUtilityTests {

  @Test
  void cannotBeInstantiated() {
    assertThatThrownBy(StringUtility::new)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  @Test
  @DisplayName("fromSnakeCaseToUpperCamelCase() returns ThisIsATest for THIS_IS_A_TEST")
  void fromSnakeCaseToUpperCamelCaseReturnsThisIsATestForThisIsATest1() {
    assertThat(StringUtility.fromSnakeCaseToUpperCamelCase("THIS_IS_A_TEST"))
        .isEqualTo("ThisIsATest");
  }

  @Test
  @DisplayName("fromSnakeCaseToUpperCamelCase() returns ThisIsATest for __THIS__IS_A_TEST__")
  void fromSnakeCaseToUpperCamelCaseReturnsThisIsATestForThisIsATest2() {
    assertThat(StringUtility.fromSnakeCaseToUpperCamelCase("__THIS__IS_A_TEST__"))
        .isEqualTo("ThisIsATest");
  }

  @Test
  @DisplayName(
      "fromSnakeCaseToUpperCamelCase() returns 1230this15is2A3Test for 1_2_3_0THIS_1_5IS_2_A_3_TEST")
  void fromSnakeCaseToUpperCamelCaseReturnsThisIsATestForThisIsATest3() {
    assertThat(StringUtility.fromSnakeCaseToUpperCamelCase("1_2_3_0THIS_1_5IS_2_A_3_TEST"))
        .isEqualTo("1230this15is2A3Test");
  }

  @Test
  @DisplayName("fromSnakeCaseToCamelCase() returns thisIsATest for THIS_IS_A_TEST ")
  void fromSnakeCaseToCamelCaseReturnsThisIsATestForThisIsATest() {
    assertThat(StringUtility.fromSnakeCaseToCamelCase("THIS_IS_A_TEST")).isEqualTo("thisIsATest");
  }
}
