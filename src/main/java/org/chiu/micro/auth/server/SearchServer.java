package org.chiu.micro.auth.server;

import java.util.List;

import org.chiu.micro.auth.lang.Result;
import org.chiu.micro.auth.page.PageAdapter;
import org.chiu.micro.auth.vo.BlogDocumentVo;
import org.chiu.micro.auth.vo.BlogEntityVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SearchServer {

  @GetExchange("/public/blog")
  Result<PageAdapter<BlogDocumentVo>> selectBlogsByES(@RequestParam(value = "currentPage", required = false) Integer currentPage, @RequestParam(value = "allInfo") Boolean allInfo, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "keywords") String keywords);

  @GetExchange("/sys/blogs")
  Result<PageAdapter<BlogEntityVo>> searchAllBlogs(@RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer size, @RequestParam(value = "keywords") String keywords, @RequestParam Long userId, @RequestBody List<String> roles);


  
}
