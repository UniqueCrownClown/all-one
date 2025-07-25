package com.one.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.dto.OneContentDTO;
import com.one.mapper.ArticleContentMapper;
import com.one.mapper.ArticleMapper;
import com.one.po.Article;
import com.one.po.ArticleContent;
import com.one.service.ArticleService;
import com.one.vo.ArticleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Override
    public ArticleVO getArticleById(long id) {

        ArticleVO articleVO = new ArticleVO();
        LambdaQueryWrapper<ArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleContent::getArticleId, id);
        ArticleContent articleContent = articleContentMapper.selectOne(queryWrapper);
        Article article = articleMapper.selectById(id);
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setContent(articleContent.getContent());
        articleVO.setKeyword(articleContent.getKeyword());
        return articleVO;
    }

    @Override
    public void customRemoveById(Long id) {
        articleMapper.deleteById(id);

        // 构建删除条件，根据 article_id 字段删除 ArticleContent 表中的记录
        LambdaQueryWrapper<ArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleContent::getArticleId, id);
        articleContentMapper.delete(queryWrapper);
    }

    @Override
    public void saveArticle(OneContentDTO oneContentDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(oneContentDTO, article);
        article.setCreatedTime(LocalDateTime.now());
        article.setPublishTime(LocalDateTime.now());
        articleMapper.insert(article);
        ArticleContent articleContent = new ArticleContent();
        articleContent.setArticleId(article.getId());
        articleContent.setContent(oneContentDTO.getContent());
        articleContent.setKeyword(oneContentDTO.getKeyword());
        articleContentMapper.insert(articleContent);
    }

    @Override
    public void updateArticle(OneContentDTO oneContentDTO, Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            BeanUtils.copyProperties(oneContentDTO, article);
            article.setPublishTime(LocalDateTime.now());
            articleMapper.updateById(article);
        }
        LambdaQueryWrapper<ArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleContent::getArticleId, id);
        ArticleContent articleContent = articleContentMapper.selectOne(queryWrapper);
        articleContent.setContent(oneContentDTO.getContent());
        articleContent.setKeyword(oneContentDTO.getKeyword());
        articleContentMapper.updateById(articleContent);
    }
}
