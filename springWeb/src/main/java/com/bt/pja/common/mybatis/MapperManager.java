package com.bt.pja.common.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Created by chenjiayao on 2016/5/24.
 */
public class MapperManager implements BeanFactoryAware {
    BeanFactory beanFactory;

    /**
     * 支持多库的Mapper
     */
    public <T> T getMapper(String db, Class<T> mapperClass) {
        String sqlSessionFactoryName = MybatisInitializationHelper.genSqlSessionFactoryBeanName(db);
        SqlSessionFactory factory = SqlSessionFactory.class.cast(beanFactory.getBean(sqlSessionFactoryName));
        if(!factory.getConfiguration().getMapperRegistry().hasMapper(mapperClass)){
            factory.getConfiguration().getMapperRegistry().addMapper(mapperClass);
        }
        /**
         * 注意这里面不要直接: SqlSession session=factory.openSession()
         * 此时session的实例是DefaultSqlSession，而不是SqlSessionTemplate
         * 如果是DefaultSqlSession，那么session没有办法close。而SqlSessionTemplate执行每一个方法
         * 之后就自动close。
         */
        @SuppressWarnings("resource")
		SqlSession session =new SqlSessionTemplate(factory);
        return session.getMapper(mapperClass);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}