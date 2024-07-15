package org.chiu.micro.auth.provider;

import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.utils.SecurityAuthenticationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping(value = "/inner")
@RequiredArgsConstructor
@Validated
public class AuthProvider {

    private final SecurityAuthenticationUtils securityAuthenticationUtils;

    @GetMapping("/auth/{token}")
    @SneakyThrows
    public Result<Authentication> findById(@PathVariable @NotBlank String token) {
        Authentication authentication = securityAuthenticationUtils.getAuthentication(token);
        return Result.success(authentication);
    }

}
