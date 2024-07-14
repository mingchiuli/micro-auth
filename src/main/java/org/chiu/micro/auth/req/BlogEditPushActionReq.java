package org.chiu.micro.auth.req;

import lombok.Data;

@Data
public class BlogEditPushActionReq {

    private Long id;

    //内容变动的部分
    private String contentChange;

    private Integer operateTypeCode;

    private Integer version;

    private Integer indexStart;

    private Integer indexEnd;

    private String field;

    private Integer paraNo;
}
