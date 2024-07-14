package org.chiu.micro.auth.req;

import lombok.Data;

import java.util.List;

@Data
public class UserEntityReq {

    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String password;

    private String email;

    private String phone;

    private Integer status;

    private List<String> roles;
}
