package org.example.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "org.example.study.repository")
@EntityScan(basePackages = "org.example.study.entity") // 显式扫描实体类包
@SpringBootApplication
public class StudyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyServiceApplication.class, args);
    }

}