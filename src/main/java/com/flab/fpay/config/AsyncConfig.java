package com.flab.fpay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean(name = "historyThreadPoolExecutor")
    public Executor eventListenerThreadPoolExecutor(
            @Value("${thread-pool.history.size}") int corePoolSize,
            @Value("${thread-pool.history.max-size}") int maxPoolSize,
            @Value("${thread-pool.history.queue-capacity}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix("History-Thread-Pool-Executor");
        return taskExecutor;
    }
}
