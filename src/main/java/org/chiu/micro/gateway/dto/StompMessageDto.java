package org.chiu.micro.gateway.dto;

import lombok.Data;

@Data
public class StompMessageDto {

    private Integer version;

    private Long userId;

    private Long blogId;

    private Integer type;
}
