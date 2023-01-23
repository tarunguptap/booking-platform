package com.organization.config;

import com.organization.entity.Seat;
import com.organization.service.LockingExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${com.organization.booking.lockExecutorCount}")
    private int lockExecutorCount;

    @Value("${com.organization.zookeeper.path}")
    private String zookeeperPath;

    @Value("${esb.zookeeper.cluster}")
    private String zookeeperCluster;

    @Bean
    public int lockExecutorCount() {
        return lockExecutorCount;
    }

    @Bean
    public LockingExecutor<Seat> lockingExecutor(int lockExecutorCount) {
        return new LockingExecutor<>(zookeeperCluster, zookeeperPath, lockExecutorCount);
    }
}
