package com.one.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.one.dto.EmployeeDTO;
import com.one.dto.EmployeeLoginDTO;
import com.one.dto.EmployeePageQueryDTO;
import com.one.po.Employee;
import com.one.result.PageResult;

public interface EmployeeService extends IService<Employee> {
    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用账户
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据iD查询用户信息
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 编辑信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
