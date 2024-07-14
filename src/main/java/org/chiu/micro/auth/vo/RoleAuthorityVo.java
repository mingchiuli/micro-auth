package org.chiu.micro.auth.vo;

import lombok.Data;

@Data
public class RoleAuthorityVo {

    private Long authorityId;

    private String code;

    private Boolean check;
}
