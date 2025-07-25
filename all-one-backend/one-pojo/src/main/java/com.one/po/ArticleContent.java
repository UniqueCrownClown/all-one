package com.one.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("article_content")
public class ArticleContent implements Serializable {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文章id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 文章关键字
     */
    private String keyword;

    /**
     * 文章内容
     */
    private String content;
}
