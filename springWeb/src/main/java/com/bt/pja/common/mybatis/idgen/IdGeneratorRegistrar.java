package com.bt.pja.common.mybatis.idgen;


import com.bt.pja.common.mybatis.MybatisInitializationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;


/**
 * Created by chenjiapeng on 2016/5/31 0031.
 */
class IdGeneratorRegistrar implements ImportBeanDefinitionRegistrar {
	private static final Logger LOG = LoggerFactory.getLogger(IdGeneratorRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
		Map<String, Object> attrs = annotationMetadata.getAnnotationAttributes(EnableIdGenerator.class.getTypeName());
		String[] idGenDbs = (String[]) attrs.get("dbs");
		try {
			if (idGenDbs != null && idGenDbs.length > 0) {
				for (String idGenDb : idGenDbs) {
					MybatisInitializationHelper.registerMapper(beanDefinitionRegistry, idGenDb, IdGeneratorMapper.class);
					IdGeneratorRegistrarHelper.registerIdGenerator(beanDefinitionRegistry, idGenDb);
				}
			}
		} catch (Exception e) {
			LOG.error("初始化IdGenerator失败！idGenDbs：{}", idGenDbs, e);
		}
	}


}
