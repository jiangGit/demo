package com.bt.pja.common.mybatis.interceptors;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * 获取 delete,update,insert执行的sql拦截器 执行mapper操作之后，通过getSql获取sql
 * 
 * @author huangyongsheng
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class TakeModifySqlInterceptor implements Interceptor {
	private static final ThreadLocal<String> CurrentSql = new ThreadLocal<String>();
	private static final String[] PrintSqlIds = { "delete", "update", "insert", "replace" };

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue = null;
		returnValue = invocation.proceed();
		String sqlId = mappedStatement.getId();
		for (String newPrintSqlId : PrintSqlIds) {
			if (sqlId.toLowerCase().contains(newPrintSqlId.toLowerCase())) {
				String sql = showSql(configuration, boundSql);
				CurrentSql.set(sql);
				break;
			}
		}
		return returnValue;
	}

	public static String getSql() {
		String sql = CurrentSql.get();
		CurrentSql.remove();
		return sql;
	}

	private static String getParameterValue(Object obj) {
		String value = null;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}

		}
		return value;
	}

	private static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = StringUtils.replaceOnce(sql, "?", getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = StringUtils.replaceOnce(sql, "?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = StringUtils.replaceOnce(sql, "?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties0) {
	}
}