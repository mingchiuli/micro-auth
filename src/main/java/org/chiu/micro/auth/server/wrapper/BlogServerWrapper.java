package org.chiu.micro.auth.server.wrapper;

import java.util.List;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.HttpServletResponse;

import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.page.PageAdapter;
import org.chiu.micro.auth.req.BlogEditPushAllReq;
import org.chiu.micro.auth.req.BlogEntityReq;
import org.chiu.micro.auth.req.DeleteBlogsReq;
import org.chiu.micro.auth.req.ImgUploadReq;
import org.chiu.micro.auth.server.BlogServer;
import org.chiu.micro.auth.utils.SecurityUtils;
import org.chiu.micro.auth.vo.BlogDeleteVo;
import org.chiu.micro.auth.vo.BlogEditVo;
import org.chiu.micro.auth.vo.BlogEntityVo;
import org.springframework.http.MediaType;
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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sys/blog")
public class BlogServerWrapper {

    private final BlogServer blogServer;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:blog:save')")
    public Result<Void> saveOrUpdate(@RequestBody BlogEntityReq blog) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.saveOrUpdate(blog, userId);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:blog:delete')")
    public Result<Void> deleteBlogs(@RequestBody List<Long> ids) {
        var req = new DeleteBlogsReq();
        req.setIds(ids);
        req.setRoles(SecurityUtils.getLoginRole());
        req.setUserId(SecurityUtils.getLoginUserId());
        return blogServer.deleteBatch(req);
    }

    @GetMapping("/lock/{blogId}")
    @PreAuthorize("hasAuthority('sys:blog:lock')")
    public Result<String> setBlogToken(@PathVariable(value = "blogId") Long blogId) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.setBlogToken(blogId, userId);
    }

    @GetMapping("/blogs")
    @PreAuthorize("hasAuthority('sys:blog:blogs')")
    public Result<PageAdapter<BlogEntityVo>> getAllBlogs(@RequestParam(required = false) Integer currentPage,
                                                         @RequestParam(required = false) Integer size) {
        List<String> roles = SecurityUtils.getLoginRole();
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.findAllABlogs(currentPage, size, userId, roles);
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('sys:blog:deleted')")
    public Result<PageAdapter<BlogDeleteVo>> getDeletedBlogs(@RequestParam Integer currentPage,
                                                             @RequestParam Integer size) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.findDeletedBlogs(currentPage, size, userId);
    }

    @GetMapping("/recover/{idx}")
    @PreAuthorize("hasAuthority('sys:blog:recover')")
    public Result<Void> recoverDeletedBlog(@PathVariable(value = "idx") Integer idx) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.recoverDeletedBlog(idx, userId);
    }

    @PostMapping("/oss/upload")
    @PreAuthorize("hasAuthority('sys:blog:oss:upload')")
    @SneakyThrows
    public Result<String> uploadOss(@RequestParam MultipartFile image) {
        Long userId = SecurityUtils.getLoginUserId();
        ImgUploadReq req = new ImgUploadReq();
        req.setData(image.getBytes());
        req.setFileName(image.getOriginalFilename());
        return blogServer.uploadOss(req, userId);
    }

    @GetMapping("/oss/delete")
    @PreAuthorize("hasAuthority('sys:blog:oss:delete')")
    public Result<Void> deleteOss(@RequestParam String url) {
        return blogServer.deleteOss(url);
    }

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('sys:blog:download')")
    @SneakyThrows
    public void download(HttpServletResponse response) {
        byte[] data = blogServer.download();
        response.setContentLength(data.length);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @PostMapping("/edit/push/all")
    @PreAuthorize("hasAuthority('sys:blog:push:all')")
    public Result<Void> pullSaveBlog(@RequestBody BlogEditPushAllReq blog) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.pushAll(blog, userId);
    }

    @GetMapping("/edit/pull/echo")
    @PreAuthorize("hasAuthority('sys:blog:echo')")
    public Result<BlogEditVo> getEchoDetail(@RequestParam(value = "blogId", required = false) Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        return blogServer.findEdit(id, userId);
    }

}
