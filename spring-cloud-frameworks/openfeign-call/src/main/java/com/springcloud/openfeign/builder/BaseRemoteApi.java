package com.springcloud.openfeign.builder;

import org.springframework.web.bind.annotation.RequestMapping;

// 没有@FeignClient注解，服务名称动态地通过FeignClientBuilder.forType()来设置
public interface BaseRemoteApi {

    @RequestMapping("/api/info")
    String info(String gid);
}
