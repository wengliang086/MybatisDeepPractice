package com.my.test.mybatis.test;

import com.my.test.mybatis.bean.Employee;
import com.my.test.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {

    public static void main(String[] args) throws IOException {
        // 1、创建 SqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 2、获取 SqlSession
        SqlSession session = sqlSessionFactory.openSession();
        try {
            query_interface(session);
//            query_base(session);
        } finally {
            session.close();
        }
    }

    private static void query_interface(SqlSession session) {
        // 没有提供接口实现，Mybatis会自动创建一个代理对象
        EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
        Employee employee = mapper.selectEmployeeById(1);
        System.out.println(employee);
    }

    private static void query_base(SqlSession session) {
        /**
         * statement 可以是 mapper 中的 select id 或者 namespace + "." + select id
         */
        // Employee employee = session.selectOne("com.my.test.mybatis.bean.Employee.selectEmployeeById", 1);
        Employee employee = session.selectOne("selectEmployeeById", 1);
        System.out.println(employee);
    }
}
