package org.chiu.micro.gateway.config;

import java.util.concurrent.TimeUnit;

import org.springframework.web.socket.messaging.WebSocketStompClient;

import jakarta.annotation.PostConstruct;

import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class WebSocketStompSessionWrapper {

    private final WebSocketStompClient webSocketStompClient;

    private final StompSessionHandlerAdapter stompSessionHandlerAdapter;

    @Getter
    private StompSession stompSession;

    @PostConstruct
    @SneakyThrows
    private void init() {
        stompSession = webSocketStompClient.connectAsync("ws://micro-websocket:8081/edit/ws", stompSessionHandlerAdapter).get(10, TimeUnit.SECONDS);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    @SneakyThrows
    private void task() {
        if (!stompSession.isConnected()) {
            stompSession = webSocketStompClient.connectAsync("ws://micro-websocket:8081/edit/ws", stompSessionHandlerAdapter).get(10, TimeUnit.SECONDS);
        }
    }

}
