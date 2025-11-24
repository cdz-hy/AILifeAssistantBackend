package org.example.aianalysis.config;

import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.jobs.mappers.JobMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JobRunr配置类
 * 使用 jobrunr-spring-boot-3-starter 时，只需提供 StorageProvider Bean，
 * Starter 会自动配置 JobRunr、JobScheduler 和 BackgroundJobServer。
 **/
@Configuration
public class JobRunrConfig {
    
    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        InMemoryStorageProvider storageProvider = new InMemoryStorageProvider();
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }
}