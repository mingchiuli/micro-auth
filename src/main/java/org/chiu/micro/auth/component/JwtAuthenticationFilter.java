package org.chiu.micro.auth.component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.chiu.micro.auth.exception.AuthException;
import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.utils.SecurityAuthenticationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final ObjectMapper objectMapper;

    private final SecurityAuthenticationUtils securityAuthenticationUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   ObjectMapper objectMapper,
                                   SecurityAuthenticationUtils securityAuthenticationUtils) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.securityAuthenticationUtils = securityAuthenticationUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication;

        try {
            authentication = securityAuthenticationUtils.getAuthentication(jwt);
        } catch (AuthException e) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(
                    objectMapper.writeValueAsString(
                            Result.fail(e.getMessage())));
            return;
        }

        // 非白名单资源、接口都要走这个流程，没有set就不能访问
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
