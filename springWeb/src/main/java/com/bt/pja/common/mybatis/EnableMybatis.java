
package com.bt.pja.common.mybatis;


import com.bt.pja.common.reader.ClassPathPropertiesReader;
import com.bt.pja.common.reader.PropertiesReader;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * 启用mybatis功能
 *
 * @author huangyongsheng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MybatisImportRegistrar.class)
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)//上面事务开启需要这个开关
public @interface EnableMybatis {
	/**
	 * 初始化数据库名字列表,多个库以“,”号隔开
	 *
	 * @return
	 */
	String dbs();
	
	/**
	 * 扫描mapper类的包
	 *
	 * @return
	 */
	String scanMapperPackage() ;
	

	/**
	 * 拦截器
	 *
	 * @return
	 */
	Class<? extends Interceptor>[] interceptors() default {};



	/**
	 * 数据库配置文件，配置用户名、密码、连接url等
	 *
	 * @return
	 */
	String configPath() default "db.properties";


	Class<? extends PropertiesReader> propertiesReader() default ClassPathPropertiesReader.class;
}
