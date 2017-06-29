package com.bt.pja.common.mybatis;

import com.bt.pja.common.exception.SpringInitException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Created by chenjiapeng on 2016/5/31 0031.
 */
class MapperScanner extends ClassPathBeanDefinitionScanner {
	public MapperScanner(BeanDefinitionRegistry registry) {
		super(registry, true);
		this.addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
	}

	@Override
	protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		BeanDefinition definition = definitionHolder.getBeanDefinition();
		try {
			if (definition instanceof AnnotatedBeanDefinition) {
				AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
				AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
				AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(Mapper.class.getName(), false));
				if (attributes != null) {
					String db = attributes.getString("db");
					if (db != null && db.length() > 0) {
						 MybatisInitializationHelper.registerMapper(registry, db, Class.forName(definition.getBeanClassName()));
					}
				}
			}
		} catch (Exception e) {
			throw new SpringInitException("", e);
		}
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	}
}
