package net.flyingfishflash.ledger.core.utilities;

import java.util.ArrayList;
import java.util.List;

import net.flyingfishflash.ledger.core.Messages;

public final class ExceptionUtility {

  @SuppressWarnings("java:S1118")
  public ExceptionUtility() {
    throw new UnsupportedOperationException(Messages.Error.CANNOT_INSTANTIATE_CLASS.value());
  }

  public static Throwable exceptionCause(Throwable throwable) {
    if (throwable == null)
      throw new IllegalArgumentException(Messages.Error.NULL_CONSTRUCTOR_ARG.value());
    var rootCause = throwable;
    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
      rootCause = rootCause.getCause();
    }
    return rootCause;
  }

  public static List<ProblemDetailUtility.ExceptionCauseDetail> extractCauses(Throwable throwable) {

    ProblemDetailUtility.ExceptionCauseDetail exceptionCauseDetail;
    List<ProblemDetailUtility.ExceptionCauseDetail> exceptionCauseDetails = new ArrayList<>();

    if (throwable.getCause() != null) {
      Throwable rootCause = exceptionCause(throwable);
      exceptionCauseDetail =
          new ProblemDetailUtility.ExceptionCauseDetail(
              rootCause.getClass().getSimpleName(), rootCause.getLocalizedMessage());
      exceptionCauseDetails.add(exceptionCauseDetail);
    }
    return exceptionCauseDetails;
  }
}
