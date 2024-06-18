package org.chiu.micro.gateway.token;

import lombok.Data;

import java.util.List;

@Data
public class Claims {

    private String userId;

    private List<String> roles;
}
