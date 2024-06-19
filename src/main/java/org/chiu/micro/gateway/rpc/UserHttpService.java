package org.chiu.micro.gateway.rpc;

import java.util.List;

import org.chiu.micro.gateway.dto.RoleEntityDto;
import org.chiu.micro.gateway.dto.UserEntityDto;
import org.chiu.micro.gateway.lang.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface UserHttpService {

    @GetExchange("/user/status/{username}/{status}")
    void changeUserStatusByUsername(@PathVariable(value = "username") String username, @PathVariable(value = "code") Integer status);

    @PostExchange("/user/role/{status}")
    Result<List<RoleEntityDto>> findByRoleCodeInAndStatus(@RequestBody List<String> roles, @PathVariable(value = "status") Integer status);

    @PostExchange("/user/login/time/{username}")
    void updateLoginTime(@PathVariable String username);

    @GetExchange("/user/email/{email}")
    Result<UserEntityDto> findByEmail(@PathVariable(value = "email") String email);

    @GetExchange("/user/phone/{phone}")
    Result<UserEntityDto> findByPhone(@PathVariable(value = "phone") String phone);

    @GetExchange("/user/role/{userId}")
    Result<List<String>> findRoleCodesDecorByUserId(@PathVariable(value = "userId") Long userId);

    @GetExchange("/user/{userId}")
    Result<UserEntityDto> findById(@PathVariable(value = "userId") Long userId);

    @GetExchange("/user/login/query/{username}")
    Result<UserEntityDto> findByUsernameOrEmailOrPhone(@PathVariable(value = "username") String username);

    @PostExchange("/user/role/authority")
    Result<List<String>> getAuthoritiesByRoleCodes(@RequestBody List<String> rawRoles);
  
}
