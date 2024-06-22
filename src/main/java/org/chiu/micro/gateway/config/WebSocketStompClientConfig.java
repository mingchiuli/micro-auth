package org.chiu.micro.gateway.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
public class WebSocketStompClientConfig {

    private final StompSessionHandlerAdapter sessionHandlerAdapter;

    @Bean
    @SneakyThrows
    StompSession stompSession() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        SimpleAsyncTaskScheduler taskScheduler = new SimpleAsyncTaskScheduler();
        taskScheduler.setVirtualThreads(true);
        stompClient.setTaskScheduler(taskScheduler); // for heartbeats
        return stompClient.connectAsync("ws://micro-websocket:8081/edit/ws", sessionHandlerAdapter).get();
    }

    @Bean("subscriptionMap")
    Map<String, Subscription> subscription() {
        return new ConcurrentHashMap<>();
    }
    
}
