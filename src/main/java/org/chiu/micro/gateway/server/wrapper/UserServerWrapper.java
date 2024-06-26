package org.chiu.micro.gateway.server.wrapper;

import org.chiu.micro.gateway.lang.Result;
import org.chiu.micro.gateway.page.PageAdapter;
import org.chiu.micro.gateway.req.AuthorityEntityReq;
import org.chiu.micro.gateway.req.MenuEntityReq;
import org.chiu.micro.gateway.req.RoleEntityReq;
import org.chiu.micro.gateway.req.UserEntityRegisterReq;
import org.chiu.micro.gateway.req.UserEntityReq;
import org.chiu.micro.gateway.server.UserServer;
import org.chiu.micro.gateway.utils.SecurityUtils;
import org.chiu.micro.gateway.vo.AuthorityVo;
import org.chiu.micro.gateway.vo.MenuDisplayVo;
import org.chiu.micro.gateway.vo.MenuEntityVo;
import org.chiu.micro.gateway.vo.MenusAndButtonsVo;
import org.chiu.micro.gateway.vo.RoleAuthorityVo;
import org.chiu.micro.gateway.vo.RoleEntityVo;
import org.chiu.micro.gateway.vo.RoleMenuVo;
import org.chiu.micro.gateway.vo.UserEntityVo;


import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletOutputStream;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * UserServerWrapper
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sys")
public class UserServerWrapper {

    private final UserServer userServer;

    @GetMapping("/authority/list")
    @PreAuthorize("hasAuthority('sys:authority:list')")
    public Result<List<AuthorityVo>> findAllAuthority() {
        return userServer.findAllAuthority();
    }


    @GetMapping("/authority/info/{id}")
    @PreAuthorize("hasAuthority('sys:authority:info')")
    public Result<AuthorityVo> findByAuthorityId(@PathVariable(value = "id") Long id) {
        return userServer.findByAuthorityId(id);
    }

    @PostMapping("/authority/save")
    @PreAuthorize("hasAuthority('sys:authority:save')")
    public Result<Void> saveOrUpdateAuthority(@RequestBody AuthorityEntityReq req) {
        return userServer.saveOrUpdateAuthority(req);
    }

    @PostMapping("/authority/delete")
    @PreAuthorize("hasAuthority('sys:authority:delete')")
    public Result<Void> deleteAuthorities(@RequestBody List<Long> ids) {
        return userServer.deleteAuthorities(ids);
    }

    @GetMapping("/authority/download")
    @PreAuthorize("hasAuthority('sys:authority:download')")
    public Result<Void> downloadAuthorities() {
        userServer.downloadAuthorities();
        return Result.success();
    }

    @GetMapping("/menu/nav")
    @PreAuthorize("hasAuthority('sys:menu:nav')")
    public Result<MenusAndButtonsVo> nav() {
        List<String> roles = SecurityUtils.getLoginRole();
        return userServer.getCurrentUserNav(roles);
    }

    @GetMapping("/menu/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public Result<MenuEntityVo> findByMenuId(@PathVariable(name = "id") Long id) {
        return userServer.findByMenuId(id);
    }

    @GetMapping("/menu/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<MenuDisplayVo>> listMenu() {
        return userServer.menuTree();
    }

    @PostMapping("/menu/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result<Void> saveOrUpdateMenu(@RequestBody MenuEntityReq menu) {
        return userServer.saveOrUpdateMenu(menu);
    }

    @PostMapping("/menu/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<Void> deleteMenu(@PathVariable("id") Long id) {
        return userServer.deleteMenu(id);
    }

    @GetMapping("/menu/download")
    @PreAuthorize("hasAuthority('sys:menu:download')")
    @SneakyThrows
    public void downloadMenu(HttpServletResponse response) {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        byte[] data = userServer.downloadMenu();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/role/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result<RoleEntityVo> info(@PathVariable("id") Long id) {
        return userServer.infoRole(id);
    }

    @GetMapping("/role/roles")
    @PreAuthorize("hasAuthority('sys:role:roles')")
    public Result<PageAdapter<RoleEntityVo>> getPage(@RequestParam(required = false) Integer currentPage,
                                                     @RequestParam(required = false) Integer size) {
        return userServer.getRolePage(currentPage, size);
    }

    @PostMapping("/role/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result<Void> saveOrUpdateRole(@RequestBody RoleEntityReq role) {
        return userServer.saveOrUpdateRole(role);
    }

    @PostMapping("/role/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<Void> deleteRole(@RequestBody List<Long> ids) {
        return userServer.deleteRole(ids);
    }

    @PostMapping("/role/menu/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:menu:save')")
    public Result<Void> saveMenu(@PathVariable("roleId") Long roleId,
                                 @RequestBody List<Long> menuIds) {
        return userServer.saveMenu(roleId, menuIds);
    }

    @GetMapping("/role/menu/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:menu:get')")
    public Result<List<RoleMenuVo>> getMenusInfo(@PathVariable Long roleId) {
        return userServer.getMenusInfo(roleId);
    }

    @PostMapping("/role/authority/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:authority:save')")
    public Result<Void> saveAuthority(@PathVariable("roleId") Long roleId,
                                      @RequestBody List<Long> authorityIds) {
        return userServer.saveAuthority(roleId, authorityIds);
    }

    @GetMapping("/role/authority/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:authority:get')")
    public Result<List<RoleAuthorityVo>> getAuthoritiesInfo(@PathVariable Long roleId) {
        return userServer.getAuthoritiesInfo(roleId);
    }

    @GetMapping("/role/download")
    @PreAuthorize("hasAuthority('sys:role:download')")
    @SneakyThrows
    public void downloadRole(HttpServletResponse response) {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        byte[] data = userServer.downloadRole();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @GetMapping("/role/valid/all")
    @PreAuthorize("hasAuthority('sys:role:valid:all')")
    public Result<List<RoleEntityVo>> getValidAll() {
        return userServer.getValidAll();
    }

    @GetMapping("/user/auth/register/page")
    @PreAuthorize("hasAuthority('sys:user:register:page')")
    public Result<String> getRegisterPage(@RequestParam String username) {
        return userServer.getRegisterPage(username);
    }

    @GetMapping("/user/register/check")
    public Result<Boolean> checkRegisterPage(@RequestParam String token) {
        return userServer.checkRegisterPage(token);
    }


    @PostMapping("/user/register/save")
    public Result<Void> saveRegisterPage(@RequestParam String token,
                                         @RequestBody UserEntityRegisterReq userEntityRegisterReq) {
        return userServer.saveRegisterPage(token, userEntityRegisterReq);
    }

    @PostMapping("/user/register/image/upload")
    public Result<String> imageUpload(@RequestParam MultipartFile image,
                                      @RequestParam String token) {
        return userServer.imageUpload(token, image);
    }

    @GetMapping("/user/register/image/delete")
    public Result<Void> imageDelete(@RequestParam String url,
                                    @RequestParam String token) {
        return userServer.imageDelete(token, url);
    }

    @PostMapping("/user/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result<Void> saveOrUpdateUser(@RequestBody UserEntityReq userEntityReq) {
        return userServer.saveOrUpdateUser(userEntityReq);
    }

    @GetMapping("/user/page/{currentPage}")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<PageAdapter<UserEntityVo>> listPageUser(@PathVariable(value = "currentPage") Integer currentPage,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        return userServer.listPageUser(currentPage, size);
    }

    @PostMapping("/user/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result<Void> deleteUsers(@RequestBody List<Long> ids) {
        return userServer.deleteUsers(ids);
    }

    @GetMapping("/user/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:info')")
    public Result<UserEntityVo> findByIdUser(@PathVariable(value = "id") Long id) {
        return userServer.findByIdUser(id);
    }

    @GetMapping("/user/download")
    @PreAuthorize("hasAuthority('sys:user:download')")
    @SneakyThrows
    public void downloadUser(HttpServletResponse response) {
        ServletOutputStream outputStream = response.getOutputStream();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        byte[] data = userServer.downloadUser();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }
}