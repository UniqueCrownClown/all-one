package com.one;

import com.one.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
public class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void batchRefreshCreateTime() {
        LocalDateTime now = LocalDateTime.now();
        AtomicReference<Short> i = new AtomicReference<>((short) 0);
        articleMapper.selectList(null).forEach(article -> {
            article.setCreatedTime(now.plusSeconds(i.get()));
            articleMapper.updateById(article);
            i.getAndSet((short) (i.get() + 1));
            System.out.println(i);
        });
    }
}
