package org.chiu.micro.gateway.rpc.wrapper;

import static org.chiu.micro.gateway.lang.ExceptionMessage.*;

import java.util.List;

import org.chiu.micro.gateway.dto.RoleEntityDto;
import org.chiu.micro.gateway.dto.UserEntityDto;
import org.chiu.micro.gateway.exception.MissException;
import org.chiu.micro.gateway.lang.Result;
import org.chiu.micro.gateway.rpc.UserHttpService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserHttpServiceWrapper {

    private final UserHttpService userHttpService;

    public void changeUserStatusByUsername(String username, Integer status) {
        userHttpService.changeUserStatusByUsername(username, status);
    }

    public List<RoleEntityDto> findByRoleCodeInAndStatus(List<String> roles, Integer status) {
        Result<List<RoleEntityDto>> result = userHttpService.findByRoleCodeInAndStatus(roles, status);
        return result.getData();
    }

    public void updateLoginTime(String username) {
        userHttpService.updateLoginTime(username);
    }

    public void findByEmail(String loginEmail) {
        Result<UserEntityDto> result = userHttpService.findByEmail(loginEmail);
        if (result.getCode() != 200) {
            throw new MissException(NO_FOUND.getMsg());
        }
    }

    public void findByPhone(String loginSMS) {
        Result<UserEntityDto> result = userHttpService.findByPhone(loginSMS);
        if (result.getCode() != 200) {
            throw new MissException(NO_FOUND.getMsg());
        }
    }

    public List<String> findRoleCodesDecorByUserId(Long userId) {
        Result<List<String>> result = userHttpService.findRoleCodesDecorByUserId(userId);
        return result.getData();
    }

    public UserEntityDto findById(Long userId) {
        Result<UserEntityDto> result = userHttpService.findById(userId);
        return result.getData();
    }

    public UserEntityDto findByUsernameOrEmailOrPhone(String username) {
        Result<UserEntityDto> result = userHttpService.findByUsernameOrEmailOrPhone(username);
        return result.getData();
    }

    public List<String> getAuthoritiesByRoleCodes(List<String> rawRoles) {
        Result<List<String>> result = userHttpService.getAuthoritiesByRoleCodes(rawRoles);
        return result.getData();
    }

}
