package net.flyingfishflash.ledger.unit.core.response.structure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.net.URI;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import net.flyingfishflash.ledger.core.Messages;
import net.flyingfishflash.ledger.core.response.structure.Disposition;
import net.flyingfishflash.ledger.core.response.structure.Response;

/** Unit tests for {@link Response} */
@DisplayName("Response")
class ResponseTests {

  @Test
  void calcSizeForList() {
    assertThat(
            new Response<>(
                    Arrays.asList("Item", "Item"),
                    "Lorem Ipsum",
                    "Lorem Ipsum",
                    URI.create("lorem/ipsum"))
                .getSize())
        .isEqualTo(2);
  }

  @Test
  void calcSizeForMap() {
    Map<String, Integer> map =
        Stream.of(
                new AbstractMap.SimpleImmutableEntry<>("lorem", 1),
                new AbstractMap.SimpleImmutableEntry<>("ipsum", 2))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    assertThat(
            new Response<>(map, "Lorem Ipsum", "Lorem Ipsum", URI.create("lorem/ipsum")).getSize())
        .isEqualTo(2);
  }

  @Test
  void calcDisposition() {
    assertThat(
            new Response<>(ProblemDetail.forStatus(404), "Lorem Ipsum", "Lorem Ipsum")
                .getDisposition())
        .isEqualTo(Disposition.FAILURE);

    assertThat(
            new Response<>(ProblemDetail.forStatus(500), "Lorem Ipsum", "Lorem Ipsum")
                .getDisposition())
        .isEqualTo(Disposition.ERROR);

    assertThat(
            new Response<>("Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum", URI.create("lorem/ipsum"))
                .getDisposition())
        .isEqualTo(Disposition.SUCCESS);
  }

  @Nested
  @DisplayName("(T content, String message, String method, URI instance)")
  class Constructor1 {

    @Test
    void constructorPreconditions() {
      assertThatIllegalArgumentException()
          .isThrownBy(() -> new Response<>(null, null, null, null))
          .withMessage(Messages.Error.NULL_CONSTRUCTOR_ARG.value());

      assertThatIllegalArgumentException()
          .isThrownBy(
              () ->
                  new Response<>(
                      ProblemDetail.forStatus(418),
                      "Lorem Ipsum",
                      "Lorem Ipsum",
                      URI.create("lorem/ipsum")))
          .withMessage("Content must not be of type ProblemDetail");
    }
  }

  @Nested
  @DisplayName("(T content, String message, String method)")
  class Constructor2 {

    @Test
    void constructorPreconditions() {
      assertThatIllegalArgumentException()
          .isThrownBy(() -> new Response<>(null, null, null))
          .withMessage(Messages.Error.NULL_CONSTRUCTOR_ARG.value());

      assertThatIllegalArgumentException()
          .isThrownBy(() -> new Response<>("Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum"))
          .withMessage("Content must be of type ProblemDetail");
    }
  }
}
