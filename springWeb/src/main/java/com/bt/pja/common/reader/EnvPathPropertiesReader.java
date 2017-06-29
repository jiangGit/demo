package com.bt.pja.common.reader;

import com.bt.pja.common.exception.SpringInitException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/***
 * 根据envPath.properties配置的路径来找文件,配置解释如下：
 env.path=file:/home/tech/config 这是相对于根目录
 env.path=classpath: 这是相对于环境变量路径

 * @author huangyongsheng
 */
public class EnvPathPropertiesReader implements PropertiesReader {
	@Override
	public Properties readProperties(String path) {
		try {
			return loadPropertiesByEnvPath(path);
		} catch (Exception e) {
			throw new SpringInitException("读取配置文件出错，file=" + path, e);
		}
	}

	public static Properties loadPropertiesByEnvPath(String propertiesFileInEnvPath) throws IOException {
		Properties envPathProperties = PropertiesLoaderUtils.loadAllProperties("envPath.properties");
		ResourceLoader loader = new DefaultResourceLoader();
		String envPath=System.getProperty("myEnvPath",envPathProperties.get("env.path").toString());
		return PropertiesLoaderUtils.loadProperties(loader.getResource(normalizePath(envPath+"/"+propertiesFileInEnvPath)));
	}

	private static String normalizePath(String envPath) {
		return envPath.replace("//", "/");
	}
	
}
