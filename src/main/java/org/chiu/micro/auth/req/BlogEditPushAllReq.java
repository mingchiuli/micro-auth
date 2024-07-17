package org.chiu.micro.auth.req;

import lombok.Data;

import java.util.List;

@Data
public class BlogEditPushAllReq {

    private Long id;

    private String title;

    private String description;

    private String content;

    private Integer status;

    private String link;

    private Integer version;

    private List<String> sensitiveContentList;
}
