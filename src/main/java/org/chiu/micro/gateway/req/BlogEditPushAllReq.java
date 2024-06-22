package org.chiu.micro.gateway.req;

import lombok.Data;

@Data
public class BlogEditPushAllReq {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private Integer status;

    private String link;

    private Integer version;
}
