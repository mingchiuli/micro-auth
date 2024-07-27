package org.chiu.micro.auth.vo;

import lombok.Data;

import java.util.List;


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

    private List<SensitiveContentVo> sensitiveContentList;
}
