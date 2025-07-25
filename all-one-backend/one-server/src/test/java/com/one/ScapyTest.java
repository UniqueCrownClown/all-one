package com.one;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.one.dto.OneContentDTO;
import com.one.dto.OneImageDTO;
import com.one.dto.OnePageDTO;
import com.one.po.Article;
import com.one.po.ArticleContent;
import com.one.po.ImageConfig;
import com.one.mapper.ArticleContentMapper;
import com.one.service.ArticleService;
import com.one.service.ImageConfigService;
import com.one.service.WebScraperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class ScapyTest {

    @Autowired
    private WebScraperService webScraperService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleContentMapper articleContentMapper;

    @Autowired
    private ImageConfigService imageConfigService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Spring Test 默认会在测试方法结束时回滚事务，以保持数据库状态的干净。
     * */
    @Test
    @Transactional
    @Commit// 提交事务
    public void testOne() {

        OnePageDTO onePage = webScraperService.fetchOnePage("https://wufazhuce.com/");

        List<OneImageDTO> images = onePage.getImages();
        LocalDateTime baseTime = LocalDateTime.now();

        // 时间增量，单位为毫秒
        long increment = 1;

        for (OneImageDTO image : images) {
            LambdaQueryWrapper<ImageConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            String order = image.getOrder();

            ImageConfig one = imageConfigService.getOne(lambdaQueryWrapper.eq(order != null, ImageConfig::getLabels, order));

            if (one != null) {
                continue;
            }
            ImageConfig imageConfig = new ImageConfig();
            BeanUtils.copyProperties(image, imageConfig);
            imageConfig.setLabels(image.getOrder());
            imageConfig.setCreatedTime(baseTime.minusSeconds(increment));
            increment += 1;
            imageConfigService.save(imageConfig);
        }

        List<OneContentDTO> content = onePage.getContents();

        for (OneContentDTO oneContentDTO : content) {
            LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            String order = oneContentDTO.getOrder();
            Article one = articleService.getOne(lambdaQueryWrapper.eq(order != null, Article::getLabels, order));
            if (one != null) {
                continue;
            }
            Article article = new Article();
            article.setType(oneContentDTO.getType());
            article.setAuthor(oneContentDTO.getAuthor());
            article.setLabels(oneContentDTO.getOrder());
            article.setTitle(oneContentDTO.getTitle());
            article.setStaticUrl(oneContentDTO.getUrl());
            LocalDateTime localDateTime = baseTime.minusSeconds(increment);
            article.setCreatedTime(localDateTime);
            article.setPublishTime(localDateTime);
            increment += 1;
            articleService.save(article);

            if (oneContentDTO.getContent() != null) {
                ArticleContent articleContent = new ArticleContent();
                articleContent.setArticleId(article.getId());
                articleContent.setContent(oneContentDTO.getContent());
                articleContent.setKeyword(oneContentDTO.getKeyword());
                articleContentMapper.insert(articleContent);
            }
        }

    }

}
