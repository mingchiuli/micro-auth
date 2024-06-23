package org.chiu.micro.gateway.config.interceptor;

import com.nimbusds.jose.jwk.source.JWKSetParseException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;

import org.chiu.micro.gateway.key.KeyFactory;
import org.chiu.micro.gateway.token.Claims;
import org.chiu.micro.gateway.token.TokenUtils;
import org.chiu.micro.gateway.utils.SecurityAuthenticationUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.chiu.micro.gateway.lang.Const.*;
import static org.chiu.micro.gateway.lang.ExceptionMessage.*;

/**
 * @author mingchiuli
 * @create 2022-06-17 9:46 PM
 */
@Component
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {

    private final TokenUtils<Claims> tokenUtils;

    private final SecurityAuthenticationUtils securityAuthenticationUtils;

    private final StompSession stompSession;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Qualifier("subscriptionMap")
    private final Map<String, Subscription> subscriptionMap;

    @Override
    @SneakyThrows
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            String jwt;
            try {
                jwt = token.substring(TOKEN_PREFIX.getInfo().length());
            } catch (IndexOutOfBoundsException e) {
                throw new JWKSetParseException(TOKEN_INVALID.getMsg(), null);
            }

            Claims claims = tokenUtils.getVerifierByToken(jwt);
            String userId = claims.getUserId();
            List<String> roles = claims.getRoles();

            Authentication authentication = securityAuthenticationUtils.getAuthentication(roles, userId);
            accessor.setUser(authentication);
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String token = accessor.getFirstNativeHeader("Authorization");
        String jwt = token.substring(TOKEN_PREFIX.getInfo().length());
        Claims claims = tokenUtils.getVerifierByToken(jwt);
        Long userId = Long.valueOf(claims.getUserId());
        String blogId = accessor.getFirstNativeHeader("Identity");
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String destination = KeyFactory.createPushContentIdentityKey(userId, Long.valueOf(blogId));
            Subscription pushSubscription = stompSession.subscribe("/edits/push/" + destination, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    simpMessagingTemplate.convertAndSend("/edits/push/" + destination, payload);
                }
            });
            Subscription pullSubscription = stompSession.subscribe("/edits/pull/" + destination, new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    simpMessagingTemplate.convertAndSend("/edits/pull/" + destination, payload);
                }
            });

            String subscriptionKey = KeyFactory.createSubscriptionKey(userId, blogId);
            subscriptionMap.put(PUSH.getInfo() + subscriptionKey, pushSubscription);
            subscriptionMap.put(PULL.getInfo() + subscriptionKey, pullSubscription);
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            String subscriptionKey = KeyFactory.createSubscriptionKey(userId, blogId);
            Subscription pushSubscription = subscriptionMap.get(PUSH.getInfo() + subscriptionKey);
            Subscription pullSubscription = subscriptionMap.get(PULL.getInfo() + subscriptionKey);
            pushSubscription.unsubscribe();
            pullSubscription.unsubscribe();
            subscriptionMap.remove(PUSH.getInfo() + subscriptionKey);
            subscriptionMap.remove(PULL.getInfo() + subscriptionKey);
        }
    }
}
