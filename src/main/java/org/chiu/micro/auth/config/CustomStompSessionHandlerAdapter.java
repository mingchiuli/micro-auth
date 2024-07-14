package org.chiu.micro.auth.config;

import static org.chiu.micro.auth.lang.MessageEnum.*;

import java.lang.reflect.Type;
import java.util.Optional;

import org.chiu.micro.auth.dto.StompMessageDto;
import org.chiu.micro.auth.key.KeyFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomStompSessionHandlerAdapter extends StompSessionHandlerAdapter {
  
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
	public @NonNull Type getPayloadType(@NonNull StompHeaders headers) {
		return StompMessageDto.class;
	}

    @Override
	public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
        session.subscribe("/edits/msg", this);
	}

    @Override
	public void handleFrame(@NonNull StompHeaders headers, @Nullable Object payload) {
        StompMessageDto dto = (StompMessageDto) payload;
        Optional.ofNullable(dto).ifPresent(message -> {
            Integer type = message.getType();
            Integer version = message.getVersion();
            String userKey = KeyFactory.createPushContentIdentityKey(message.getUserId(), message.getBlogId());

            if (PUSH_ALL.getCode().equals(type)) {
                simpMessagingTemplate.convertAndSend("/edits/push/" + userKey, version.toString());
                return;
            }

            if (PULL_ALL.getCode().equals(type)) {
                simpMessagingTemplate.convertAndSend("/edits/pull/" + userKey, version.toString());
            }
        });
	}
}
