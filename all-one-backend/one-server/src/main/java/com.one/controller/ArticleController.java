package com.one.controller;

import com.one.controller.core.BaseController;
import com.one.dto.OneContentDTO;
import com.one.po.Article;
import com.one.result.Result;
import com.one.service.ArticleService;
import com.one.vo.ArticleVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "文章模块")
public class ArticleController extends BaseController<Article> {

    private final ArticleService articleService;

    @GetMapping("/detail/{id}")
    public Result<ArticleVO> articleById(@PathVariable long id) {
        ArticleVO articleVO = articleService.getArticleById(id);
        return Result.success(articleVO);
    }

    /*
     * 新增
     *
     * */
    @PostMapping("/save")
    public Result<String> save(@RequestBody OneContentDTO oneContentDTO) {
        articleService.saveArticle(oneContentDTO);
        return Result.success();
    }

    /*
     * 修改
     *
     * */
    @PutMapping("/update/{id}")
    public Result<String> update(@RequestBody OneContentDTO oneContentDTO, @PathVariable Long id) {
        articleService.updateArticle(oneContentDTO,id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {

        articleService.customRemoveById(id);
        return Result.success();
    }

}
