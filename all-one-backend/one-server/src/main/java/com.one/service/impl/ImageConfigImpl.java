package com.one.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.po.ImageConfig;
import com.one.mapper.ImageConfigMapper;
import com.one.service.ImageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageConfigImpl extends ServiceImpl<ImageConfigMapper, ImageConfig> implements ImageConfigService {

    private final ImageConfigMapper imageConfigMapper;

}