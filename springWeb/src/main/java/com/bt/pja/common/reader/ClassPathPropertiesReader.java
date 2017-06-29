package com.bt.pja.common.reader;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/***
 * 相对于classpath目录的文件
 * @author huangyongsheng
 *
 */
public class ClassPathPropertiesReader implements PropertiesReader {
	@Override
	public Properties readProperties(String path) {
		try {
			return PropertiesLoaderUtils.loadAllProperties(path);
		} catch (IOException e) {
			throw new RuntimeException("配置文件加载失败, path=" + path);
		}
	}
}
