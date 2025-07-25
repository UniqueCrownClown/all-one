package com.one.service;

import com.one.dto.OneContentDTO;
import com.one.po.Article;
import com.one.vo.ArticleVO;
import com.one.service.core.BaseService;

public interface ArticleService extends BaseService<Article> {
    ArticleVO getArticleById(long id);

    void customRemoveById(Long id);

    void saveArticle(OneContentDTO oneContentDTO);

    void updateArticle(OneContentDTO oneContentDTO, Long id);
}
