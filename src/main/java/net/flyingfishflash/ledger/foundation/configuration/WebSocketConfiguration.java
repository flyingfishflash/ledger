package net.flyingfishflash.ledger.foundation.configuration;

import java.util.List;
import javax.servlet.annotation.WebListener;
import net.flyingfishflash.ledger.foundation.WebSocketSessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
// public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
public class WebSocketConfiguration
    extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketConfiguration.class);

  private WebSocketSessionId webSocketSessionId;

  public WebSocketConfiguration(WebSocketSessionId webSocketSessionId) {
    this.webSocketSessionId = webSocketSessionId;
  }

  @Override
  // public void registerStompEndpoints(StompEndpointRegistry registry) {
  public void configureStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOrigins("*").setAllowedOrigins("*");
    logger.info("register stomp endpoints");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/user/", "/import/");
    registry.setPreservePublishOrder(true);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(
        new ChannelInterceptor() {

          /**
           * Invoked after the completion of a send regardless of any exception that have been
           * raised thus allowing for proper resource cleanup.
           *
           * <p>Note that this will be invoked only if preSend successfully completed and returned a
           * Message, i.e. it did not return {@code null}.
           *
           * @since 4.1
           */
          @Override
          public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            assert accessor != null;
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
              List<String> authorization = accessor.getNativeHeader("authorization");
              assert authorization != null;
            }

            return message;
          }
        });
  }

  @EventListener
  private void handleSessionConnected(SessionConnectEvent event) {
    String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
    logger.info("* Session Connect Event: " + event.getMessage());
    // TODO: Should I add the websocket session id to the http session attributes here?
    webSocketSessionId.setSessionId(sessionId);
  }

  @WebListener
  public class MyRequestContextListener extends RequestContextListener {}
}
