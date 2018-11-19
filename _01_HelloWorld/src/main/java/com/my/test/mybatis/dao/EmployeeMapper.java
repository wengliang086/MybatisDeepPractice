package com.my.test.mybatis.dao;

import com.my.test.mybatis.bean.Employee;

public interface EmployeeMapper {

    Employee selectEmployeeById(int id);
}
