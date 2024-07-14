package org.chiu.micro.auth.req;

import lombok.Data;
import java.util.List;


/**
 * DeleteBlogsReq
 */
@Data
public class DeleteBlogsReq {

    private List<Long> ids;

    private List<String> roles;

    private Long userId;
}