package com.xueyi.common.redis.configure.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson配置
 *
 * @author xueyi
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    /** redis缓存key前缀 */
    private String keyPrefix;

    /** 线程池数量,默认值 = 当前处理核数量 * 2 */
    private int threads = 4;

    /** Netty线程池数量,默认值 = 当前处理核数量 * 2 */
    private int nettyThreads = 8;

    /** 单机服务配置 */
    private SingleServerConfig singleServerConfig = new SingleServerConfig();

    /** 集群服务配置 */
    private ClusterServersConfig clusterServersConfig;

    @Data
    @NoArgsConstructor
    public static class SingleServerConfig {

        /** 客户端名称 */
        private String clientName;

        /** 最小空闲连接数 */
        private int connectionMinimumIdleSize = 8;

        /** 连接池大小 */
        private int connectionPoolSize = 32;

        /** 连接空闲超时，单位：毫秒 */
        private int idleConnectionTimeout = 10000;

        /** 命令等待超时，单位：毫秒 */
        private int timeout = 3000;

        /** 发布和订阅连接池大小 */
        private int subscriptionConnectionPoolSize = 50;

    }

    @Data
    @NoArgsConstructor
    public static class ClusterServersConfig {

        /** 客户端名称 */
        private String clientName;

        /** master最小空闲连接数 */
        private int masterConnectionMinimumIdleSize;

        /** master连接池大小 */
        private int masterConnectionPoolSize;

        /** slave最小空闲连接数 */
        private int slaveConnectionMinimumIdleSize;

        /** slave连接池大小 */
        private int slaveConnectionPoolSize;

        /** 连接空闲超时，单位：毫秒 */
        private int idleConnectionTimeout;

        /** 命令等待超时，单位：毫秒 */
        private int timeout;

        /** 发布和订阅连接池大小 */
        private int subscriptionConnectionPoolSize;

        /** 读取模式 */
        private ReadMode readMode;

        /** 订阅模式 */
        private SubscriptionMode subscriptionMode;

    }
}
