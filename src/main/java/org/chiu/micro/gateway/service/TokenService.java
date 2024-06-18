package org.chiu.micro.gateway.service;

import org.chiu.micro.gateway.vo.UserInfoVo;

import java.util.Map;

/**
 * @author mingchiuli
 * @create 2023-03-30 4:29 am
 */
public interface TokenService {

    Map<String, String> refreshToken();

    UserInfoVo userinfo();
}
