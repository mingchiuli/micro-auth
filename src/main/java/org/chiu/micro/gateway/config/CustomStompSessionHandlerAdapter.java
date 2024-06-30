package org.chiu.micro.gateway.config;

import java.lang.reflect.Type;

import org.chiu.micro.gateway.dto.StompMessageDto;
import org.chiu.micro.gateway.key.KeyFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@SuppressWarnings("null")
@Slf4j
public class CustomStompSessionHandlerAdapter extends StompSessionHandlerAdapter {
  
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
	public Type getPayloadType(StompHeaders headers) {
		return StompMessageDto.class;
	}

    @Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/edits/msg", this);
	}

    @Override
    @SneakyThrows
	public void handleFrame(StompHeaders headers, Object payload) {
        StompMessageDto message = (StompMessageDto) payload;
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
}
