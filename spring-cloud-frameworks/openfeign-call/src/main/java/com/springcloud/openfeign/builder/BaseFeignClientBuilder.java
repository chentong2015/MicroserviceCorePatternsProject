package com.springcloud.openfeign.builder;

import com.springcloud.openfeign.client.authorization.DefaultAuthorizationClient;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;

public class BaseFeignClientBuilder {

    // 创建指定类型的Feign Client, 通过调用方法来发送请求
    private static void test(ApplicationContext applicationContext) {
        FeignClientBuilder clientBuilder = new FeignClientBuilder(applicationContext);
        DefaultAuthorizationClient feignClient = clientBuilder
                .forType(DefaultAuthorizationClient.class, "testClient")
                .url("base url for the request")
                .build();
        feignClient.checkPermission("", "", "");
    }
}
