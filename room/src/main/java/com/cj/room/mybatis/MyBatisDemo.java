package com.cj.room.mybatis;

import com.cj.room.bean.User;
import com.cj.room.druid.DruidDataSourceFactory;
import com.cj.room.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by liuchunjiang on 2017/6/14.
 */
public class MyBatisDemo {

    public SqlSessionFactory createSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    public SqlSessionFactory createSqlSessionFactory2() throws IOException {
        DruidDataSourceFactory dataSourceFactory = new DruidDataSourceFactory();
        Properties properties = new Properties();
        properties.load( Resources.getResourceAsStream("druid.properties"));
        dataSourceFactory.setProperties(properties);
        DataSource dataSource = dataSourceFactory.getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        return sqlSessionFactory;
    }

    public void test(){
        try {
            SqlSession session = createSqlSessionFactory().openSession();
            try {
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user =mapper.selectByPrimaryKey(1);
                System.out.println(user.getUserName());
            } finally {
                session.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            SqlSession session = createSqlSessionFactory2().openSession();
            try {
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user =mapper.selectByPrimaryKey(1);
                System.out.println(user.getUserName());
            } finally {
                session.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyBatisDemo demo = new MyBatisDemo();
        demo.test();
    }

}
