package net.flyingfishflash.ledger.unit.core.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.utilities.IdentifierUtility;

/** Unit tests for {@link net.flyingfishflash.ledger.core.utilities.IdentifierUtility} */
@DisplayName("IdentifierUtility")
class IdentifierUtilityTests {

  @Test
  void cannotBeInstantiated() {
    assertThatThrownBy(IdentifierUtility::new)
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  @Test
  void identifierDoesNotContainHyphens() {
    assertThat(IdentifierUtility.identifier()).doesNotContain("-");
  }

  @Test
  void identifierWithHyphensContainsHyphens() {
    assertThat(IdentifierUtility.identifierWithHyphens()).contains("-");
  }
}
