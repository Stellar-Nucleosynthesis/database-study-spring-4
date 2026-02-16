package org.example.practice5.config;

import org.example.practice5.util.DataSourceEnum;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(primaryDataSourceConfig.getDriverClassName());
        dataSource.setUrl(primaryDataSourceConfig.getUrl());
        dataSource.setUsername(primaryDataSourceConfig.getUsername());
        dataSource.setPassword(primaryDataSourceConfig.getPassword());
        return dataSource;
    }

    public DataSource replicaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(replicaDataSourceConfig.getDriverClassName());
        dataSource.setUrl(replicaDataSourceConfig.getUrl());
        dataSource.setUsername(replicaDataSourceConfig.getUsername());
        dataSource.setPassword(replicaDataSourceConfig.getPassword());
        return dataSource;
    }
}