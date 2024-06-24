package org.chiu.micro.gateway.config;

import java.lang.reflect.Type;

import org.chiu.micro.gateway.dto.StompMessageDto;
import org.chiu.micro.gateway.key.KeyFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class CustomStompSessionHandlerAdapter extends StompSessionHandlerAdapter {
  
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ObjectMapper objectMapper;

    private Subscription subscription;

    @Override
	public Type getPayloadType(StompHeaders headers) {
		return String.class;
	}

    @Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        subscription = session.subscribe("/edits/msg", this);
	}

    @Override
    @SneakyThrows
	public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("!!!");
        StompMessageDto message = objectMapper.readValue((String) payload,StompMessageDto.class);
        Integer type = message.getType();
        Integer version = message.getVersion();
        String userKey = KeyFactory.createPushContentIdentityKey(message.getUserId(), message.getBlogId());

        if (Integer.valueOf(-1).equals(type)) {
            simpMessagingTemplate.convertAndSend("/edits/push/" + userKey, version.toString());
            return;
        }

        if (Integer.valueOf(-2).equals(type)) {
            simpMessagingTemplate.convertAndSend("/edits/pull/" + userKey, version.toString());
        }
        
	}

    @Override
	public void handleTransportError(StompSession session, Throwable exception) {
        subscription.unsubscribe();
	}


}
