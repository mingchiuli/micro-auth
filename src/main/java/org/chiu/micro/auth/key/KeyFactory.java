package org.chiu.micro.auth.key;

import static org.chiu.micro.auth.lang.Const.*;

import java.util.Objects;

public class KeyFactory {

    private KeyFactory() {}

    public static String createPushContentIdentityKey(Long userId, Long blogId) {
        return Objects.isNull(blogId) ?
                userId.toString() :
                userId + "/" + blogId;
    }

    public static String createBlogEditRedisKey(Long userId, Long blogId) {
        return Objects.isNull(blogId) ?
                TEMP_EDIT_BLOG.getInfo() + userId :
                TEMP_EDIT_BLOG.getInfo() + userId + ":" + blogId;
    }
}
