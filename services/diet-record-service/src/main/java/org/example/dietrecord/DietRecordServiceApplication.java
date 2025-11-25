package org.example.dietrecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DietRecordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DietRecordServiceApplication.class, args);
    }

}