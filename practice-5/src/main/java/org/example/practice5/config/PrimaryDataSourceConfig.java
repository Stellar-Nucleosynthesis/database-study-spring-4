package org.example.practice5.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.datasource.primary")
@Data
public class PrimaryDataSourceConfig {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer maximumPoolSize;
    private Integer connectionTimeout;
}
