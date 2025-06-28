package com.sky.service;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);


    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    PageResult PageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 设置员工状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return 员工信息
     */
    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
