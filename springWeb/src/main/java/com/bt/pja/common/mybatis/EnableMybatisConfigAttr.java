
package com.bt.pja.common.mybatis;

import com.bt.pja.common.reader.PropertiesReader;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;


/**
 * {@link EnableMybatis}每一个属性对应了这个Bean的字段
 */
class EnableMybatisConfigAttr{
	private static final String DB_CONF_DEFAULT_PROPERTIES = "dbconf_default.properties";
	
	String[] dbs;
	Class<? extends Interceptor>[] interceptors;
	String scanMapperPackage;
	Properties configProperties;
	
	@SuppressWarnings("unchecked")
	public EnableMybatisConfigAttr(Map<String, Object> attrs) throws Exception {
		this.dbs=getString("dbs",attrs).split(",");
		this.interceptors=(Class<? extends Interceptor>[]) attrs.get("interceptors");
		this.scanMapperPackage=getString("scanMapperPackage",attrs);
		Class<? extends PropertiesReader> reader=(Class<? extends PropertiesReader>) attrs.get("propertiesReader");
		this.configProperties = initDbDefaultProperties();
		String dbPropertiesPath = getString("configPath",attrs);
		Properties dbProperties = reader.newInstance().readProperties(dbPropertiesPath);
		if(dbProperties==null || dbProperties.isEmpty()){
			throw new IllegalArgumentException("配置文件："+dbPropertiesPath+"不存在或者配置为空");
		}
		this.configProperties.putAll(dbProperties);
		
	}
	
	private static Properties initDbDefaultProperties() throws IOException {
		return PropertiesLoaderUtils.loadAllProperties(DB_CONF_DEFAULT_PROPERTIES);
	}
	private String getString(String key, Map<String, Object> attrs) {
		return (String) attrs.get(key);
	}
	
}
