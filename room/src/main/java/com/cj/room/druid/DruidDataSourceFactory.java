package com.cj.room.druid;

import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class DruidDataSourceFactory implements DataSourceFactory {
    private DataSource dataSource;

    @Override
    public void setProperties(Properties properties) {
        try {
            this.dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(properties);
        } catch (RuntimeException var3) {
            throw var3;
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new RuntimeException(var4.getMessage());
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }
}
