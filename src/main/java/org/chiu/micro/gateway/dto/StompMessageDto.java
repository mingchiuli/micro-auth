package org.chiu.micro.gateway.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StompMessageDto implements Serializable {

    private Integer version;

    private Long userId;

    private Long blogId;

    private Integer type;
}
