package org.chiu.micro.auth.req;

import lombok.Data;

@Data
public class UserEntityRegisterReq {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String password;

    private String confirmPassword;

    private String email;

    private String phone;
}
