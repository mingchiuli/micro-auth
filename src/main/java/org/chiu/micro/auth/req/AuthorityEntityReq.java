package org.chiu.micro.auth.req;

import lombok.Data;


@Data
public class AuthorityEntityReq {

    private Long id;

    private String name;

    private String code;

    private String remark;

    private Integer status;
}
