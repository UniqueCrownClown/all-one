package com.one.controller;


import com.one.constant.JwtClaimsConstant;
import com.one.dto.EmployeeDTO;
import com.one.dto.EmployeeLoginDTO;
import com.one.dto.EmployeePageQueryDTO;
import com.one.po.Employee;
import com.one.vo.EmployeeLoginVO;
import com.one.properties.JwtProperties;
import com.one.result.PageResult;
import com.one.result.Result;
import com.one.service.EmployeeService;
import com.one.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "管理相关接口")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("后台登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用账户
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用账户")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用禁用账户：{}，{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据iD查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据iD查询用户信息")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据iD查询用户信息：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑信息
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("编辑信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}
