package org.chiu.micro.auth.utils;

import lombok.RequiredArgsConstructor;

import org.chiu.micro.auth.dto.AuthDto;
import org.chiu.micro.auth.exception.AuthException;
import org.chiu.micro.auth.rpc.wrapper.UserHttpServiceWrapper;
import org.chiu.micro.auth.token.Claims;
import org.chiu.micro.auth.token.TokenUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.chiu.micro.auth.lang.Const.*;
import static org.chiu.micro.auth.lang.ExceptionMessage.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationUtils {

    private final UserHttpServiceWrapper userHttpServiceWrapper;

    private final TokenUtils<Claims> tokenUtils;

    private final StringRedisTemplate redisTemplate;

    private Authentication getAuthentication(List<String> roles, String userId) {
        List<String> rawRoles = new ArrayList<>();
        roles.forEach(role -> rawRoles.add(role.substring(ROLE_PREFIX.getInfo().length())));
        List<String> authorities = userHttpServiceWrapper.getAuthoritiesByRoleCodes(rawRoles);
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userId, null, AuthorityUtils.createAuthorityList(authorities));
        authenticationToken.setDetails(rawRoles);

        return authenticationToken;
    }

     public Authentication getAuthentication(String token) throws AuthException {
        AuthDto authDto = getAuthDto(token);
        return getAuthentication(authDto.getAuthorities(), authDto.getUserId());
    }

    public AuthDto getAuthDto(String token) throws AuthException {
        String jwt = token.substring(TOKEN_PREFIX.getInfo().length());
        Claims claims = tokenUtils.getVerifierByToken(jwt);
        String userId = claims.getUserId();
        List<String> roles = claims.getRoles();

        String mark = redisTemplate.opsForValue().get(BLOCK_USER.getInfo() + userId);

        if (StringUtils.hasLength(mark)) {
            throw new AuthException(RE_LOGIN.getMsg());
        }

        return AuthDto.builder()
                .userId(userId)
                .authorities(roles)
                .build();
        
    }
}
