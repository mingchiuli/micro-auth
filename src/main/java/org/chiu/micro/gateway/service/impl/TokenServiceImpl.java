package org.chiu.micro.gateway.service.impl;

import org.chiu.micro.gateway.token.Claims;
import org.chiu.micro.gateway.token.TokenUtils;
import org.chiu.micro.gateway.utils.SecurityUtils;
import org.chiu.micro.gateway.vo.UserInfoVo;
import org.chiu.micro.gateway.convertor.UserInfoVoConvertor;
import org.chiu.micro.gateway.dto.UserEntityDto;
import org.chiu.micro.gateway.rpc.wrapper.UserHttpServiceWrapper;
import org.chiu.micro.gateway.service.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.chiu.micro.gateway.lang.Const.TOKEN_PREFIX;

/**
 * @author mingchiuli
 * @create 2023-03-30 4:29 am
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenUtils<Claims> tokenUtils;

    private final UserHttpServiceWrapper userHttpServiceWrapper;

    @Value("${blog.jwt.access-token-expire}")
    private long expire;

    @Override
    public Map<String, String> refreshToken() {
        Long userId = SecurityUtils.getLoginUserId();
        List<String> roleCodes = userHttpServiceWrapper.findRoleCodesDecorByUserId(userId);
        String accessToken = tokenUtils.generateToken(userId.toString(), roleCodes, expire);
        return Collections.singletonMap("accessToken", TOKEN_PREFIX.getInfo() + accessToken);
    }

    @Override
    public UserInfoVo userinfo() {
        Long userId = SecurityUtils.getLoginUserId();
        UserEntityDto userEntity = userHttpServiceWrapper.findById(userId);

        return UserInfoVoConvertor.convert(userEntity);
    }
}
