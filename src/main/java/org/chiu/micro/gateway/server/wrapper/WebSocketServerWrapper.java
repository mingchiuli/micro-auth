package org.chiu.micro.gateway.server.wrapper;

import org.chiu.micro.gateway.lang.Result;
import org.chiu.micro.gateway.req.BlogEditPushActionReq;
import org.chiu.micro.gateway.req.BlogEditPushAllReq;
import org.chiu.micro.gateway.server.WebSocketServer;
import org.chiu.micro.gateway.utils.SecurityUtils;
import org.chiu.micro.gateway.vo.BlogEditVo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.messaging.simp.stomp.StompSession;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * WebsocketServerWrapper
 */
@RestController
@RequiredArgsConstructor
public class WebSocketServerWrapper {

    private final StompSession stompSession;

    private final WebSocketServer webSocketServer;

    private final ObjectMapper objectMapper;

    @MessageMapping("/edit/ws/push/action")
    @PreAuthorize("hasAuthority('sys:edit:push:action')")
    @SneakyThrows
    public void pushAction(@RequestBody BlogEditPushActionReq req) {
        Long userId = SecurityUtils.getLoginUserId();
        stompSession.send("/app/edit/ws/push/action/" + userId, objectMapper.writeValueAsString(req));
    }

    @PostMapping("/edit/push/all")
    @PreAuthorize("hasAuthority('sys:blog:push:all')")
    public Result<Void> pullSaveBlog(@RequestBody BlogEditPushAllReq blog) {
        Long userId = SecurityUtils.getLoginUserId();
        return webSocketServer.pushAll(blog, userId);
    }

    @GetMapping("/edit/pull/echo")
    @PreAuthorize("hasAuthority('sys:blog:echo')")
    public Result<BlogEditVo> getEchoDetail(@RequestParam(value = "blogId", required = false) Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        return webSocketServer.findEdit(id, userId);
    }
    
}