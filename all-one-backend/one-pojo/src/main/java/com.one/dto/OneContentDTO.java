package com.one.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OneContentDTO implements Serializable {

    /*
     *文章序号
     */

    private String order;

    private String title;

    private String url;

    private String author;
    /*
     * 内容类型 1为文章 2为问题
     * */
    private short type;

    private short isCurrent;

    /*
     *文章内容
     *
     * */
    private String content;

    private String keyword;
}
