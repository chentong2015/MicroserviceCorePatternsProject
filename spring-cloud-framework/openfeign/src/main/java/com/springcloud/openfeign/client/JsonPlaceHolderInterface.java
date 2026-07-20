package com.springcloud.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// FeignClient客户端提供配置信息, 与FeignClientBuilder构建方式一致
// 1. Each Feign client is composed of a set of customizable components.
// 2. For each Feign client, a logger is created by default.
@FeignClient(value = "testClient", url = "https://localhost:8080/")
public interface JsonPlaceHolderInterface {

    // 推荐使用指定类型的Mapping注解
    @GetMapping(value = "/value")
    String getValues();

    @RequestMapping(method = RequestMethod.GET, value = "/data")
    String getResponseData();

    @RequestMapping(method = RequestMethod.DELETE, value = "/data/{id}",
            produces = "application/json")
    String deleteById(@PathVariable("id") Long id);
}
