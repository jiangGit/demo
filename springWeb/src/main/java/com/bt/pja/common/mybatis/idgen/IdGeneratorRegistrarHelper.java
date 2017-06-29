package com.bt.pja.common.mybatis.idgen;


import com.bt.pja.common.mybatis.MybatisInitializationHelper;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;


/**
 * @author chenjiapeng
 */
class IdGeneratorRegistrarHelper {
	private static final String ID_GEN_INFO_TABLE = "d_id_gen_info";
	public static void registerIdGenerator(BeanDefinitionRegistry beanDefinitionRegistry, String idGenDb) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(IdGenerator.class);
		beanDefinitionBuilder.setFactoryMethod("createIdGenerator");
		beanDefinitionBuilder.addConstructorArgReference(getIdGeneratorMapperBeanName(idGenDb));
		beanDefinitionBuilder.addConstructorArgValue(ID_GEN_INFO_TABLE);
		beanDefinitionBuilder.addConstructorArgReference(MybatisInitializationHelper.genTransactionManagerBeanName(idGenDb));
		beanDefinitionRegistry.registerBeanDefinition(getIdGeneratorBeanName(idGenDb), beanDefinitionBuilder.getBeanDefinition());
	}

	public static String getIdGeneratorBeanName(String db) {
		return db + IdGenerator.class.getSimpleName();
	}

	public static String getIdGeneratorMapperBeanName(String db) {
		return MybatisInitializationHelper.genMapperBeanName(db, IdGeneratorMapper.class);
	}

}
