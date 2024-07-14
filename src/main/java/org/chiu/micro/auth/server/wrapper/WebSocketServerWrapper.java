package org.chiu.micro.auth.server.wrapper;

import org.chiu.micro.auth.config.WebSocketStompSessionWrapper;
import org.chiu.micro.auth.key.KeyFactory;
import org.chiu.micro.auth.req.BlogEditPushActionReq;
import org.chiu.micro.auth.utils.SecurityUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * WebsocketServerWrapper
 */
@RestController
@RequiredArgsConstructor
public class WebSocketServerWrapper {

    private final WebSocketStompSessionWrapper webSocketStompSessionWrapper;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/edit/ws/push/action")
    @PreAuthorize("hasAuthority('sys:edit:push:action')")
    @SneakyThrows
    public void pushAction(@RequestBody BlogEditPushActionReq req) {
        Long userId = SecurityUtils.getLoginUserId();
        Long blogId = req.getId();
        try {
            webSocketStompSessionWrapper.getStompSession().send("/app/edit/ws/push/action/" + userId, req);
        } catch (Exception e) {
            String userKey = KeyFactory.createPushContentIdentityKey(userId, blogId);
            simpMessagingTemplate.convertAndSend("/edits/push/" + userKey, req.getVersion().toString());
        }
    }
    
}