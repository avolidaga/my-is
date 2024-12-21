package ru.se.info.tinder.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.naming.ServiceUnavailableException;

@Component
@RequiredArgsConstructor
@Log4j2
public class StompWebSocketClient {
    private final WebSocketStompClient webSocketStompClient;
    private StompSession session;

    @Value("${websocket.url}")
    private String url;

    @PostConstruct
    public void init() {
        connectToServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (session != null) session.disconnect();
        }
        ));
    }

    private void connectToServer() {
        try {
            if (session == null || !session.isConnected()) {
                StompSessionHandler sessionHandler = new ImageStompSessionHandler();
                session = webSocketStompClient.connect(url, sessionHandler).get();
            }
        } catch (Exception e) {
            log.error("Error occurred during websocket connect: {}", e.getMessage());
        }
    }


    public void sendMessage(String dest, String payload, String subDest, StompFrameHandler handler) throws ServiceUnavailableException {
        try {
            connectToServer();
            session.subscribe("/topic" + subDest, handler);
            session.send("/app" + dest, payload);

        } catch (Exception ex) {
            log.error("Failed to send message: {}", ex.getMessage());
            throw new ServiceUnavailableException("Image service unavailable");
        }
    }

    public class ImageStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Connection to image service created");
        }
    }
}
