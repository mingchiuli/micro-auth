package org.chiu.micro.gateway.server;

import java.util.List;

import org.chiu.micro.gateway.lang.Result;
import org.chiu.micro.gateway.page.PageAdapter;
import org.chiu.micro.gateway.req.BlogEntityReq;
import org.chiu.micro.gateway.req.DeleteBlogsReq;
import org.chiu.micro.gateway.vo.BlogDeleteVo;
import org.chiu.micro.gateway.vo.BlogEntityVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;


public interface BlogServer {

    @PostExchange("/save/{userId}")
    Result<Void> saveOrUpdate(@RequestBody BlogEntityReq blog, @PathVariable Long userId);

    @PostExchange("/delete")
    Result<Void> deleteBatch(@RequestBody DeleteBlogsReq req);

    @GetExchange("/lock/{blogId}/{userId}")
    Result<String> setBlogToken(@PathVariable(value = "blogId") Long blogId, @PathVariable(value = "userId") Long userId);

    @GetExchange("/blogs/{userId}")
    Result<PageAdapter<BlogEntityVo>> findAllABlogs(@RequestParam(value = "currentPage", required = false) Integer currentPage, @RequestParam(value = "size", required = false) Integer size, @PathVariable Long userId, @RequestBody List<String> roles);

    @GetExchange("/deleted/{userId}")
    Result<PageAdapter<BlogDeleteVo>> findDeletedBlogs(@RequestParam(value = "currentPage") Integer currentPage, @RequestParam(value = "size") Integer size, @PathVariable Long userId);

    @GetExchange("/recover/{idx}/{userId}")
    Result<Void> recoverDeletedBlog(@PathVariable(value = "idx") Integer idx, @PathVariable(value = "userId") Long userId);

    @PostExchange("/oss/upload/{userId}")
    Result<String> uploadOss(@RequestParam MultipartFile image, @PathVariable Long userId);

    @GetExchange("/oss/delete")
    Result<Void> deleteOss(@RequestParam String url);

    @GetMapping("/download")
    Result<Void> download();
}
