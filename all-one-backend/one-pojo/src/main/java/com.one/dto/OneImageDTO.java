package com.one.dto;

import com.one.po.ImageConfig;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OneImageDTO extends ImageConfig {

    private String order;

    private String type;

    private String url;

    private String content;

    private String imgUrl;

    private String dateStr;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;
}

