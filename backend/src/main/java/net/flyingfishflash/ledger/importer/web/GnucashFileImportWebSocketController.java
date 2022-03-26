package net.flyingfishflash.ledger.importer.web;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GnucashFileImportWebSocketController {

  private static final Logger logger =
      LoggerFactory.getLogger(GnucashFileImportWebSocketController.class);

  // private GnucashFileImportStatus gnucashFileImportStatus;
  // @Autowired private WebSocketSessionId webSocketSessionId;

  /*
    public GnucashFileImportWebSocketController(GnucashFileImportStatus gnucashFileImportStatus) {
      this.gnucashFileImportStatus = gnucashFileImportStatus;
    }
  */

  @MessageMapping("sendImportStatusMessage")
  @SendToUser("/import/status/messages")
  public String sendImportStatusMessage(@Payload String message) {
    logger.error("{}", message);
    return ZonedDateTime.now().toString() + " -- " + message;
  }

  @MessageMapping("sendImportStatusCounts")
  @SendToUser("/import/status/counts")
  public String sendImportStatusCounts(
      @Payload String message, @Header("simpSessionId") String sessionId) {
    // ObjectMapper mapper = new ObjectMapper();

    logger.info("header: {}", sessionId);
    // logger.info("websocketsessionid componenent: " + webSocketSessionId.getSessionId());

    // this.gnucashFileImportStatus.invoke();

    // String zz = xref.getGnucashFileImportStatus().getComponents().toString();

    // String json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(zz);

    // logger.info(gnucashFileImportStatus.toString());

    return "test";
  }
}
