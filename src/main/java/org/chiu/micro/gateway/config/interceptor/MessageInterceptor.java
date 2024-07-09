package org.chiu.micro.gateway.config.interceptor;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.chiu.micro.gateway.exception.AuthException;
import org.chiu.micro.gateway.token.Claims;
import org.chiu.micro.gateway.token.TokenUtils;
import org.chiu.micro.gateway.utils.SecurityAuthenticationUtils;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static org.chiu.micro.gateway.lang.Const.*;

/**
 * @author mingchiuli
 * @create 2022-06-17 9:46 PM
 */
@Component
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {

    private final TokenUtils<Claims> tokenUtils;

    private final SecurityAuthenticationUtils securityAuthenticationUtils;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    
        if (Objects.nonNull(accessor) && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization")).orElse("");
            String jwt = token.substring(TOKEN_PREFIX.getInfo().length());
            Claims claims;
            try {
                claims = tokenUtils.getVerifierByToken(jwt);
            } catch (AuthException e) {
                return message;
            }
            String userId = claims.getUserId();
            List<String> roles = claims.getRoles();

            Authentication authentication = securityAuthenticationUtils.getAuthentication(roles, userId);
            accessor.setUser(authentication);
        }

        return message;
    }

}
