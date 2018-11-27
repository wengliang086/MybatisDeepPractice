package com.my.test.mybatis.test;

import com.my.test.mybatis.bean.Employee;
import com.my.test.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

public class MyBatisTest {

    private SqlSessionFactory sqlSessionFactory = null;

    @Before
    public void before() {
        // 1、创建 SqlSessionFactory
        try {
            String resource = "mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectEmployeeById(1);
            System.out.println(employee);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void insert() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Employee employee = new Employee();
            employee.setLastName("J");
            employee.setGender("g");
            employee.setEmail("email@e.com");
            int insert = sqlSession.insert("insertEmployee", employee);
            sqlSession.commit();
            if (insert > 0) {
                System.out.println("Insert success!");
            }
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void cacheOne() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.selectEmployeeById(1);
            Employee employee2 = mapper.selectEmployeeById(1);
            System.out.println(employee);
            System.out.println(employee.equals(employee2));
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void cacheTwo() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        try {
            Employee employee = sqlSession.getMapper(EmployeeMapper.class).selectEmployeeById(1);
            /**
             * 注意，只有在 SqlSession 关闭的的时候，才会将其数据写入到二级缓存
             */
            sqlSession.close();

            Employee employee2 = sqlSession2.getMapper(EmployeeMapper.class).selectEmployeeById(1);
            System.out.println(employee == employee2);
            System.out.println(employee.equals(employee2));
        } finally {
            sqlSession2.close();
        }
    }
}
