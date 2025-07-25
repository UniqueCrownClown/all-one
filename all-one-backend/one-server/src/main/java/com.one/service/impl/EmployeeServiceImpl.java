package com.one.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.one.constant.MessageConstant;
import com.one.constant.PasswordConstant;
import com.one.constant.StatusConstant;
import com.one.context.BaseContext;
import com.one.dto.EmployeeDTO;
import com.one.dto.EmployeeLoginDTO;
import com.one.dto.EmployeePageQueryDTO;
import com.one.po.Employee;
import com.one.exception.AccountLockedException;
import com.one.exception.AccountNotFoundException;
import com.one.exception.PasswordErrorException;
import com.one.mapper.EmployeeMapper;
import com.one.result.PageResult;
import com.one.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getUsername, username);

        // 使用 EmployeeMapper 的 selectOne 方法执行查询
        Employee employee = employeeMapper.selectOne(queryWrapper);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增
     *
     * @param employeeDTO
     */

    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号的状态，默认正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        //设置密码，默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

//        通过ThreadLocal获取用户信息
        Long currentId = BaseContext.getCurrentId();

        //设置当前记录创建人id和修改人id
        employee.setCreateUser(currentId);//目前写个假数据，后期修改
        employee.setUpdateUser(currentId);

        employeeMapper.insert(employee);//后续步骤定义
    }


    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */

    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 创建分页对象
        Page<Employee> page = new Page<>(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (employeePageQueryDTO.getName() != null) {
            queryWrapper.like(Employee::getName, employeePageQueryDTO.getName());
        }

        // 执行分页查询
        Page<Employee> employeePage = this.page(page, queryWrapper);

        // 获取总记录数和当前页记录
        long total = employeePage.getTotal();
        List<Employee> records = employeePage.getRecords();

        return new PageResult(total, records);
    }

    /**
     * 启用禁用账户
     *
     * @param status
     * @param id
     */

    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.updateById(employee);
    }

    /**
     * 根据iD查询用户信息
     *
     * @param id
     * @return
     */

    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 编辑信息
     *
     * @param employeeDTO
     */

    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.updateById(employee);
    }

}
