package com.one.vo;

import com.one.po.Article;
import lombok.Data;

@Data
public class ArticleVO extends Article {

    private String content;

    private String keyword;

}
