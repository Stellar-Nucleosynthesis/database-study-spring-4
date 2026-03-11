package org.example.practice7.config;

import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.SocketAddressResolver;
import org.springframework.boot.data.redis.autoconfigure.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceCustomizer() {
        return builder -> builder.clientResources(
                DefaultClientResources.builder()
                        .socketAddressResolver(new SocketAddressResolver() {
                            @Override
                            public SocketAddress resolve(RedisURI uri) {
                                return new InetSocketAddress("localhost", uri.getPort());
                            }
                        })
                        .build()
        );
    }
}