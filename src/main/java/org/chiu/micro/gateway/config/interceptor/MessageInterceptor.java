package org.chiu.micro.gateway.config.interceptor;

import com.nimbusds.jose.jwk.source.JWKSetParseException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;

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

}
