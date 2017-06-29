package com.bt.pja.common.mybatis.idgen;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by chenjiapeng on 2016/5/31 0031.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(IdGeneratorRegistrar.class)
public @interface EnableIdGenerator {
	/**
	 * 启动id生成器数据库名字列表
	 */
	String[] dbs() ;
}
