package net.flyingfishflash.ledger.foundation;

import org.springframework.stereotype.Component;

@Component
// @SessionScope
public class WebSocketSessionId {

  private String sessionId;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
