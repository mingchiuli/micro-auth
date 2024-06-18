package org.chiu.micro.gateway.convertor;

import org.chiu.micro.gateway.dto.UserEntityDto;
import org.chiu.micro.gateway.vo.UserInfoVo;

public class UserInfoVoConvertor {

    private UserInfoVoConvertor() {}

    public static UserInfoVo convert(UserEntityDto userEntity) {
        return UserInfoVo.builder()
                .avatar(userEntity.getAvatar())
                .nickname(userEntity.getNickname())
                .build();
    }
}
