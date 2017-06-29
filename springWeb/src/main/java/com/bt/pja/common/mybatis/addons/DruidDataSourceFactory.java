package com.bt.pja.common.mybatis.addons;

import com.bt.pja.common.exception.SpringInitException;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by chenjiayao on 2016/5/24.
 */
public class DruidDataSourceFactory implements DataSourceFactory {
    private DataSource dataSource;

    @Override
    public void setProperties(Properties properties) {
        try {
            this.dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(properties);
        } catch (RuntimeException var3) {
            throw var3;
        } catch (Exception var4) {
            throw new SpringInitException("init data source error", var4);
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
