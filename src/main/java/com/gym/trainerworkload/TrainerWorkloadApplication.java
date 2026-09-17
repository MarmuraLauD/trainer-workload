package com.gym.trainerworkload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@EnableDiscoveryClient
public class TrainerWorkloadApplication {

    static void main(String[] args) {
        SpringApplication.run(TrainerWorkloadApplication.class, args);
    }

}
