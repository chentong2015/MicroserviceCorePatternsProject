package com.springcloud.openfeign.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 基于不同的环境Env动态的修改请的URL路径 ?
// 1. 通过RequestInterceptor拦截器来统一设置，对于上线的URL成本较大
// 2. 通过重写RouteTargeter来实现，必须考虑版本的约束
// 3. 使用DynamicFeignClientFactory动态的构建出指定的feignClient
@FeignClient(name = "feign-provider")
public interface FooFeignClient {

    @GetMapping(value = "//feign-provider-$env/foo/{username}")
    String foo1(@PathVariable("username") String username);

    @GetMapping(value = "/foo/{username}")
    String foo2(@PathVariable("username") String username);
}
