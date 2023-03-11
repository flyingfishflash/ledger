package net.flyingfishflash.ledger.core.response.structure;

/**
 * Describes the structure of error responses sent to a client<br>
 *
 * <pre>
 * response.status
 * response.content.body
 * response.content.message
 * </pre>
 */
public interface ApplicationResponse<T> {

  /**
   * @return Unique identifier intended for log entry reference
   */
  String getId();

  /**
   * @return Api Event Disposition
   */
  Disposition getDisposition();

  /**
   * @return Succinct contextual message describing an API event
   */
  String getMessage();

  /**
   * @return Response content
   */
  T getContent();

  /**
   * @return Number of items included in the content
   */
  int getSize();

  /**
   * @return Instance path
   */
  String getInstance();
}
