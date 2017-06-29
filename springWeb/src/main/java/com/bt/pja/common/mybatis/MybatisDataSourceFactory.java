package com.bt.pja.common.mybatis;

import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.util.Properties;

class MybatisDataSourceFactory {
	private static final class JdbcConf {
		private String jdbcDsFactoryName;
		private String jdbcDataSourceFactoryClass;
		private String jdbcUrl;
		private String jdbcDriver;
		private String jdbcUsername;
		private String jdbcPassword;
		private Properties jdbcDataSourceFactoryProperties=new Properties();
		public JdbcConf(Properties properties, String db) {
			if(properties.isEmpty()){
				throw new RuntimeException("数据库配置文件是空的");
			}
			this.jdbcDriver=properties.getProperty("db.jdbc.driver."+db, properties.getProperty("db.jdbc.driver.default"));
			this.jdbcDsFactoryName=properties.getProperty("db.jdbc.dsfactory."+db, properties.getProperty("db.jdbc.dsfactory.default"));
			this.jdbcUrl=properties.getProperty("db.jdbc.url."+db);
			this.jdbcUsername=properties.getProperty("db.jdbc.username."+db);
			this.jdbcPassword=properties.getProperty("db.jdbc.password."+db);
			for (Object key : properties.keySet()) {
				String keyString=key.toString().trim();
				String prefix = "db.jdbc.dsfactory."+jdbcDsFactoryName;
				if(keyString.startsWith(prefix)){
					String valueString= properties.getProperty(keyString).trim();
					if(keyString.endsWith(".class")){
						jdbcDataSourceFactoryClass=valueString;
					}else{
						String dataSourcePropertiesKey = keyString.substring(prefix.length()+1);
						jdbcDataSourceFactoryProperties.put(dataSourcePropertiesKey, valueString);
					}
				}
			}
			jdbcDataSourceFactoryProperties.put("driver", jdbcDriver);
			jdbcDataSourceFactoryProperties.put("password", jdbcPassword);
			jdbcDataSourceFactoryProperties.put("username", jdbcUsername);
			jdbcDataSourceFactoryProperties.put("url", jdbcUrl);
			if(StringUtils.isAnyBlank(jdbcDriver,jdbcDsFactoryName,jdbcUrl,jdbcUsername,jdbcPassword)){
				throw new RuntimeException("数据库配置有误db="+db);
			}
		}
	}
	
	
    public static DataSource createDatasource(String db, Properties configProperties) {
    	JdbcConf dbConf = new JdbcConf(configProperties,db);
        org.apache.ibatis.datasource.DataSourceFactory dsFactory;
        try {
            Object newInstance = Class.forName(dbConf.jdbcDataSourceFactoryClass).newInstance();
            dsFactory = org.apache.ibatis.datasource.DataSourceFactory.class.cast(newInstance);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("class Not found. [%s]", dbConf.jdbcDataSourceFactoryClass), e);
        }
        dsFactory.setProperties(dbConf.jdbcDataSourceFactoryProperties);
        return dsFactory.getDataSource();
    }


	
}