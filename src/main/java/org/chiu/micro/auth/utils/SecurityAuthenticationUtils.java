package org.chiu.micro.auth.utils;

import lombok.RequiredArgsConstructor;

import org.chiu.micro.auth.rpc.wrapper.UserHttpServiceWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.chiu.micro.auth.lang.Const.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityAuthenticationUtils {

    private final UserHttpServiceWrapper userHttpServiceWrapper;

    public Authentication getAuthentication(List<String> roles, String userId) {
        List<String> rawRoles = new ArrayList<>();
        roles.forEach(role -> rawRoles.add(role.substring(ROLE_PREFIX.getInfo().length())));
        List<String> authorities = userHttpServiceWrapper.getAuthoritiesByRoleCodes(rawRoles);
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userId, null, AuthorityUtils.createAuthorityList(authorities));
        authenticationToken.setDetails(rawRoles);

        return authenticationToken;
    }
}
