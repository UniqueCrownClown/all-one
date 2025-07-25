package com.one.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.one.controller.core.BaseController;
import com.one.dto.OneImageDTO;
import com.one.po.ImageConfig;
import com.one.result.Result;
import com.one.service.ImageConfigService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/image")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "图像模块")
public class ImageConfigController extends BaseController<ImageConfig> {

    private final ImageConfigService imageConfigService;

    @GetMapping("/page/{content}")
    public Result<Page> page(int page, int pageSize, @PathVariable String content) {
        //分页构造器,Page(第几页, 查几条)
        Page<ImageConfig> pageInfo = new Page<>(page, pageSize);
        //查询构造器
        LambdaQueryWrapper<ImageConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //过滤条件.like(什么条件下启用模糊查询，模糊查询字段，被模糊插叙的名称)
        lambdaQueryWrapper.like(!StringUtils.isEmpty(content), ImageConfig::getContent, content);
        //添加排序
        lambdaQueryWrapper.orderByDesc(ImageConfig::getCreatedTime);
        //查询分页、自动更新
        imageConfigService.page(pageInfo, lambdaQueryWrapper);
        //返回查询结果
        return Result.success(pageInfo);
    }

    /*
     * 新增图片
     *
     * */
    @PostMapping("/save")
    public Result<String> save(@RequestBody OneImageDTO oneImageDTO) {
        oneImageDTO.setCreatedTime(LocalDateTime.now());
        imageConfigService.save(oneImageDTO);
        return Result.success();
    }

    /*
     *
     * 修改图片
     *
     * */
    @PutMapping("/update")
    public Result<String> update(@RequestBody OneImageDTO oneImageDTO) {
        oneImageDTO.setUpdatedTime(LocalDateTime.now());
        imageConfigService.updateById(oneImageDTO);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result<?> detailById(@PathVariable Long id) {
        ImageConfig byId = imageConfigService.getById(id);
        return Result.success(byId);
    }

}
