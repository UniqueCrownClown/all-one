package com.one.controller.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.one.result.Result;
import com.one.service.core.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;

public class BaseController<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected BaseService<T> baseService;

    /**
     * 查询所有记录
     *
     * @return 所有记录列表
     */
    @GetMapping("/list")
    public Result<?> list() {
        List<T> list = baseService.list();
        return Result.success(list);
    }

    /**
     * 分页查询记录
     *
     * @param page     当前页码
     * @param pageSize 每页记录数
     * @return 分页记录
     */
    @PostMapping("/page")
    public Result<Page<T>> page(@RequestParam int page, @RequestParam int pageSize, @RequestBody T entity) {
        Page<T> pageParam = new Page<>(page, pageSize);
        //查询构造器
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        // 添加模糊查询条件
        if (entity != null) {
            Class<?> clazz = entity.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    String fieldName = null;
                    if (value instanceof String) {
                        fieldName = field.getName();
                        // 构建模糊查询条件
                        queryWrapper.like(fieldName, (String) value);
                    } else if (value instanceof Short) {
                        fieldName = field.getName();
                        queryWrapper.eq(fieldName, value);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("获取属性值失败", e);
                }
            }
            try {
                // 存在 createTime 字段，按该字段降序排序
                queryWrapper.orderByDesc("created_time");
            } catch (Exception e) {
                String clazzName = clazz.getSuperclass().getName();
                logger.warn("实体类 {} 未找到 created_time 字段，不进行按创建时间排序", clazzName);
            }
        }
        //查询分页、自动更新
        baseService.page(pageParam, queryWrapper);
        return Result.success(pageParam);
    }

    /**
     * 根据 ID 查询记录
     *
     * @param id 记录 ID
     * @return 对应记录
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        T byId = baseService.getById(id);
        return Result.success(byId);
    }

    /**
     * 新增记录
     *
     * @param entity 实体对象
     * @return 是否新增成功
     */
    @PostMapping
    public Result<?> save(@RequestBody T entity) {
        boolean saved = baseService.save(entity);
        return Result.success(saved);
    }

    /**
     * 更新记录
     *
     * @param entity 实体对象
     * @return 是否更新成功
     */
    @PutMapping
    public Result<?> update(@RequestBody T entity) {
        boolean updateById = baseService.updateById(entity);
        return Result.success(updateById);
    }

    /**
     * 根据 ID 删除记录
     *
     * @param id 记录 ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        baseService.removeById(id);
        return Result.success();
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        baseService.removeByIds(ids);
        return Result.success();
    }
}

