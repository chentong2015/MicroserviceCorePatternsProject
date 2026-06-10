package org.example;

import loadbalanced.EnableLoadBalancedBasicRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import properties.SpringApplicationProperties;

@SpringBootApplication
@EnableLoadBalancedBasicRestTemplate
@EnableConfigurationProperties({SpringApplicationProperties.class})
public class ResourceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceManagerApplication.class, args);
    }
}