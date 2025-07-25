package com.one.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.one.dto.UserDTO;
import com.one.po.User;
import com.one.result.Result;
import com.one.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "用户模块")
public class UserController {

    private final UserService userService;

    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @ApiOperation(value = "向客人问好")
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page<User>(page, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, User::getName, name);
        queryWrapper.orderByDesc(User::getCreateTime);
        userService.page(pageInfo);
        return Result.success(pageInfo);
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    @PostMapping("add")
    public Result<String> add(UserDTO user) {
        userService.save(user);
        return Result.success("创建成功");
    }

    @PutMapping
    public Result<String> update(UserDTO userDTO) {

        userService.updateById(userDTO);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result<String> delete(List<String> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }
}
