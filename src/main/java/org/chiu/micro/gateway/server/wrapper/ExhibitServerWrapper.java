package org.chiu.micro.gateway.server.wrapper;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.chiu.micro.gateway.lang.Result;
import org.chiu.micro.gateway.page.PageAdapter;
import org.chiu.micro.gateway.server.ExhibitServer;
import org.chiu.micro.gateway.utils.SecurityUtils;
import org.chiu.micro.gateway.vo.BlogDescriptionVo;
import org.chiu.micro.gateway.vo.BlogExhibitVo;
import org.chiu.micro.gateway.vo.BlogHotReadVo;
import org.chiu.micro.gateway.vo.VisitStatisticsVo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/public/blog")
public class ExhibitServerWrapper {

    private final ExhibitServer exhibitServer;

    @GetMapping("/info/{id}")
    public Result<BlogExhibitVo> getBlogDetail(@PathVariable(name = "id") Long id) {
        Long userId = SecurityUtils.getLoginUserId();
        List<String> roles = SecurityUtils.getLoginRole();
        return exhibitServer.getBlogDetail(id, roles, userId);
    }

    @GetMapping("/page/{currentPage}")
    public Result<PageAdapter<BlogDescriptionVo>> getPage(@PathVariable(name = "currentPage") Integer currentPage,
                                                          @RequestParam(required = false) Integer year) {
        return exhibitServer.findPage(currentPage, year);
    }

    @GetMapping("/secret/{blogId}")
    public Result<BlogExhibitVo> getLockedBlog(@PathVariable Long blogId,
                                               @RequestParam(value = "readToken") String token) {
        return exhibitServer.getLockedBlog(blogId, token);
    }

    @GetMapping("/token/{blogId}")
    public Result<Boolean> checkReadToken(@PathVariable Long blogId,
                                          @RequestParam(value = "readToken") String token) {
        return exhibitServer.checkToken(blogId, token);
    }

    @GetMapping("/status/{blogId}")
    public Result<Integer> getBlogStatus(@PathVariable Long blogId) {
        List<String> roles = SecurityUtils.getLoginRole();
        Long userId = SecurityUtils.getLoginUserId();
        return exhibitServer.getBlogStatus(blogId, roles, userId);
    }

    @GetMapping("/years")
    public Result<List<Integer>> searchYears() {
        return exhibitServer.searchYears();
    }

    @GetMapping("/stat")
    public Result<VisitStatisticsVo> getVisitStatistics() {
        return exhibitServer.getVisitStatistics();
    }

    @GetMapping("/scores")
    public Result<List<BlogHotReadVo>> getScoreBlogs() {
        return exhibitServer.getScoreBlogs();
    }
}
