package org.example;

import org.example.loadbalanced.EnableLoadBalancedBasicRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLoadBalancedBasicRestTemplate
public class ResourceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceManagerApplication.class, args);
    }
}