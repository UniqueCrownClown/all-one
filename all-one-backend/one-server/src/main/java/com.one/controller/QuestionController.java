package com.one.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.one.controller.core.BaseController;
import com.one.po.Article;
import com.one.result.Result;
import com.one.service.ArticleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "问题模块")
public class QuestionController extends BaseController<Article> {

    private final ArticleService articleService;

}
