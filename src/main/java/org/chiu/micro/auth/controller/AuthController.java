package org.chiu.micro.auth.controller;

import java.util.List;

import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.service.AuthService;
import org.chiu.micro.auth.utils.SecurityUtils;
import org.chiu.micro.auth.vo.MenusAndButtonsVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sys")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/menu/nav")
    @PreAuthorize("hasAuthority('sys:menu:nav')")
    public Result<MenusAndButtonsVo> nav() {
        List<String> roles = SecurityUtils.getLoginRole();
        return Result.success(() -> authService.getCurrentUserNav(roles));
    }
}
