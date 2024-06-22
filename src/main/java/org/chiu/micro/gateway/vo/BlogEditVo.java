package org.chiu.micro.gateway.vo;

import lombok.Data;


@Data
public class BlogEditVo {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String link;

    private String content;

    private Integer status;

    private Integer version;
}
