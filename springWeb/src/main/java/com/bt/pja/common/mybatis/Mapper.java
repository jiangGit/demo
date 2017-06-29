package com.bt.pja.common.mybatis;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repository
public @interface Mapper {
	/**数据名字***/
    String db();

}
