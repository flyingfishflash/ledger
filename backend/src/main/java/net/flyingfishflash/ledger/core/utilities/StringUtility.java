package net.flyingfishflash.ledger.core.utilities;

import java.beans.Introspector;

import net.flyingfishflash.ledger.core.Messages;

public final class StringUtility {

  @SuppressWarnings("java:S1118")
  public StringUtility() {
    throw new UnsupportedOperationException(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  public static String fromSnakeCaseToUpperCamelCase(String input) {
    StringBuilder sb = new StringBuilder();
    // TODO: Handle Numbers Prefixing Alpha Characters (1_2_3_0THIS_1_5IS_2_A_3_TEST)
    for (String s : input.split("_")) {
      if (s.length() > 0) {
        sb.append(Character.toUpperCase(s.charAt(0)));
        if (s.length() > 1) {
          sb.append(s.substring(1).toLowerCase());
        }
      }
    }
    return sb.toString();
  }

  public static String fromSnakeCaseToCamelCase(String input) {
    return Introspector.decapitalize(fromSnakeCaseToUpperCamelCase(input));
  }
}
