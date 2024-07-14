package org.chiu.micro.auth.server.wrapper;

import java.util.List;

import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.page.PageAdapter;
import org.chiu.micro.auth.server.SearchServer;
import org.chiu.micro.auth.utils.SecurityUtils;
import org.chiu.micro.auth.vo.BlogDocumentVo;
import org.chiu.micro.auth.vo.BlogEntityVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/search")
public class SearchServerWrapper {

    private final SearchServer searchServer;

    @GetMapping("/public/blog")
    public Result<PageAdapter<BlogDocumentVo>> selectBlogsByES(@RequestParam(value = "currentPage", required = false) Integer currentPage,
                                                               @RequestParam(value = "allInfo") Boolean allInfo,
                                                               @RequestParam(value = "year", required = false) String year,
                                                               @RequestParam(value = "keywords") String keywords) {
        return searchServer.selectBlogsByES(currentPage, allInfo, year, keywords);
    }

    @GetMapping("/sys/blogs")
    @PreAuthorize("hasAuthority('sys:search:blogs')")
    public Result<PageAdapter<BlogEntityVo>> searchAllBlogs(@RequestParam(required = false) Integer currentPage,
                                                            @RequestParam(required = false) Integer size,
                                                            @RequestParam(value = "keywords") String keywords) {
        Long userId = SecurityUtils.getLoginUserId();
        List<String> roles = SecurityUtils.getLoginRole();
        return searchServer.searchAllBlogs(currentPage, size, keywords, userId, roles);
    }
}
