package net.flyingfishflash.ledger.core.utilities;

import java.beans.Introspector;

public final class StringUtility {

  private StringUtility() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static String fromSnakeCaseToUpperCamelCase(String input) {
    StringBuilder sb = new StringBuilder();
    for (String s : input.split("_")) {
      sb.append(Character.toUpperCase(s.charAt(0)));
      if (s.length() > 1) {
        sb.append(s.substring(1).toLowerCase());
      }
    }
    return sb.toString();
  }

  public static String fromSnakeCaseToCamelCase(String input) {
    return Introspector.decapitalize(fromSnakeCaseToUpperCamelCase(input));
  }
}
