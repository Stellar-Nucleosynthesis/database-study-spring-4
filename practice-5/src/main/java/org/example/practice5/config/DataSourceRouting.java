package org.example.practice5.config;

import com.zaxxer.hikari.HikariDataSource;
import org.example.practice5.util.DataSourceEnum;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRouting extends AbstractRoutingDataSource {
    private final PrimaryDataSourceConfig primaryDataSourceConfig;
    private final ReplicaDataSourceConfig replicaDataSourceConfig;
    private final DataSourceContextHolder dataSourceContextHolder;

    public DataSourceRouting(
            DataSourceContextHolder dataSourceContextHolder,
            PrimaryDataSourceConfig primaryDataSourceConfig,
            ReplicaDataSourceConfig replicaDataSourceConfig) {
        this.primaryDataSourceConfig = primaryDataSourceConfig;
        this.replicaDataSourceConfig = replicaDataSourceConfig;
        this.dataSourceContextHolder = dataSourceContextHolder;

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceEnum.PRIMARY, primaryDataSource());
        dataSourceMap.put(DataSourceEnum.REPLICA, replicaDataSource());
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(primaryDataSource());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContextHolder.getBranchContext();
    }

    public DataSource primaryDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(primaryDataSourceConfig.getDriverClassName());
        ds.setJdbcUrl(primaryDataSourceConfig.getUrl());
        ds.setUsername(primaryDataSourceConfig.getUsername());
        ds.setPassword(primaryDataSourceConfig.getPassword());

        ds.setMaximumPoolSize(primaryDataSourceConfig.getMaximumPoolSize());
        ds.setConnectionTimeout(primaryDataSourceConfig.getConnectionTimeout());
        return ds;
    }

    public DataSource replicaDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(replicaDataSourceConfig.getDriverClassName());
        ds.setJdbcUrl(replicaDataSourceConfig.getUrl());
        ds.setUsername(replicaDataSourceConfig.getUsername());
        ds.setPassword(replicaDataSourceConfig.getPassword());

        ds.setMaximumPoolSize(replicaDataSourceConfig.getMaximumPoolSize());
        ds.setConnectionTimeout(replicaDataSourceConfig.getConnectionTimeout());
        return ds;
    }
}