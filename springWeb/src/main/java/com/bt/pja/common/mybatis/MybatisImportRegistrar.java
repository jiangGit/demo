package com.bt.pja.common.mybatis;

import com.bt.pja.common.exception.SpringInitException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

class MybatisImportRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableMybatis.class.getTypeName());

        try {
        	MybatisInitializationHelper.initMybatis(registry, new EnableMybatisConfigAttr(attrs));
        } catch (Exception e) {
            throw new SpringInitException("mybatis init failed.", e);
        }
    }



}