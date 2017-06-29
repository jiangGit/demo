package com.bt.pja.common.mybatis;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by chenjiayao on 2016/6/20.
 */
public class MybatisInitializationHelper {
    private static final String SQL_SESSION_FACTORY_BEAN_NAME = "%sSqlSessionFactoryName";
    private static final String TRANSATION_MANAGER_BEAN_NAME = "%sTx";

    
    public static void initMybatis(BeanDefinitionRegistry registry, EnableMybatisConfigAttr attr) {
    	try {
    		registeMapperManager(registry);
    		Interceptor[] interceptors = new Interceptor[attr.interceptors.length];
    		for (int i = 0; i < attr.interceptors.length; i++) {
    			interceptors[i] = attr.interceptors[i].newInstance();
    		}
    		for (String db : attr.dbs) {
    			DataSource dataSource=MybatisDataSourceFactory.createDatasource(db, attr.configProperties);
    			registerTransactionManager(registry, db,dataSource);
    			registerSqlSessionFactory(registry, db,dataSource,interceptors);
    		}
    		new MapperScanner(registry).scan(attr.scanMapperPackage);
		} catch (Exception e) {
			throw new RuntimeException("mybatis初始化失败",e);
		}
    }
    private static void registeMapperManager(BeanDefinitionRegistry register) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperManager.class);
        beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        register.registerBeanDefinition(MapperManager.class.getName(), beanDefinitionBuilder.getBeanDefinition());
    }
    private static void registerSqlSessionFactory(BeanDefinitionRegistry registry, String db, DataSource dataSource,
			Interceptor[] interceptors) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("dataSource", dataSource);
        beanDefinitionBuilder.addPropertyValue("plugins", interceptors);
        beanDefinitionBuilder.setLazyInit(true);
        ClassPathResource configLocation = new ClassPathResource("mybatis-config.xml");
        if (configLocation.exists()) {
            beanDefinitionBuilder.addPropertyValue("configLocation", configLocation);
        }
        registry.registerBeanDefinition(genSqlSessionFactoryBeanName(db), beanDefinitionBuilder.getBeanDefinition());
    }
    private static void registerTransactionManager(BeanDefinitionRegistry register, String db, DataSource dataSource) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class);
        beanDefinitionBuilder.addConstructorArgValue(dataSource);
        register.registerBeanDefinition(genTransactionManagerBeanName(db), beanDefinitionBuilder.getBeanDefinition());
    }

    public static void registerMapper(BeanDefinitionRegistry registry, String db, Class<?> mapperInterface) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("mapperInterface", mapperInterface);
        beanDefinitionBuilder.addPropertyReference("sqlSessionFactory", genSqlSessionFactoryBeanName(db));
        beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        beanDefinitionBuilder.setLazyInit(true);
        registry.registerBeanDefinition(genMapperBeanName(db, mapperInterface), beanDefinitionBuilder.getBeanDefinition());
    }
    public static String genTransactionManagerBeanName(String db) {
        return String.format(TRANSATION_MANAGER_BEAN_NAME, db);
    }

    public static String genMapperBeanName(String db, Class<?> mapperInterface) {
        return db + mapperInterface.getSimpleName();
    }
    public static String genSqlSessionFactoryBeanName(String db) {
        return String.format(SQL_SESSION_FACTORY_BEAN_NAME, db);
    }
}
