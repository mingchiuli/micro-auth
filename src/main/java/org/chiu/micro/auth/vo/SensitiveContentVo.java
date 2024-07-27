package org.chiu.micro.auth.vo;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class SensitiveContentVo implements Serializable {

    private Integer startIndex;

    private String content;

    private Integer type;
}
