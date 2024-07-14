package org.chiu.micro.auth.vo;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BlogDeleteVo {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String content;

    private String created;

    private String updated;

    private Integer status;

    private Integer idx;

    private String link;

    private Long readCount;
}
