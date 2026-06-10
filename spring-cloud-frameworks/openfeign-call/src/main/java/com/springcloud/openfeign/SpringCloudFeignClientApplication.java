package com.springcloud.openfeign;

import com.springcloud.openfeign.client.authorization.AuthorizationFeignClient;
import com.springcloud.openfeign.client.authorization.DefaultAuthorizationClient;
import com.springcloud.openfeign.client.authorization.DefaultAuthorizationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

// @EnableFeignClients 注解自动"注入"所有标记@FeignClient注解的类型
@SpringBootApplication
@EnableFeignClients
public class SpringCloudFeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudFeignClientApplication.class, args);
    }

    // TODO. 隐藏Request Mapping背后的逻辑
    // 1. AuthorizationFeignClient会被自动注入到Spring IoC中
    // 2. 通过Autowired从Spring IoC中获取bean之后赋值给自定义的类型实例，并注入
    // 3. 在应用中可以直接通过DefaultAuthorizationClient.checkPermission()来调用背后隐藏的@PostMapping()
    @Bean
    @Autowired
    DefaultAuthorizationClient createDefaultAuthorizationClient(AuthorizationFeignClient feignClient) {
        return new DefaultAuthorizationFeignClient(feignClient);
    }
}
