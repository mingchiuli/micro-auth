package org.chiu.micro.auth.vo;


import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class SensitiveContentVo {

    private Integer startIndex;

    private Integer endIndex;

    private Integer type;
}
