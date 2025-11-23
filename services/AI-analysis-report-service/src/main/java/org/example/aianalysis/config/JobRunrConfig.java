package org.example.aianalysis.config;

import org.jobrunr.configuration.JobRunr;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.configuration.JobRunrConfiguration.Builder;
import org.jobrunr.storage.InMemoryStorageProvider;
import org.jobrunr.storage.StorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * JobRunr配置类
 */
@Configuration
public class JobRunrConfig {
    
    private JobRunrConfiguration jobRunrConfiguration;
    
    @Bean
    public StorageProvider storageProvider() {
        return new InMemoryStorageProvider();
    }
    
    @PostConstruct
    public void initJobRunr() {
        jobRunrConfiguration = JobRunr.configure()
                .useStorageProvider(storageProvider())
                .useJsonMapper()
                .useJobActivator(applicationContext -> applicationContext)
                .initialize();
    }
    
    @PreDestroy
    public void stopJobRunr() {
        if (jobRunrConfiguration != null) {
            jobRunrConfiguration.close();
        }
    }
}