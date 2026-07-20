package com.springcloud.openfeign.client.authorization;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FunctionalInterface
@FeignClient("authz")
public interface AuthorizationFeignClient {

    // produces指要提供的数据格式，consumes指返回的消费数据格式
    @PostMapping(path = "/permissions/check",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    boolean checkPermission(@RequestHeader("Authorization") String authorizationHeader,
                            @RequestBody String permissionRequest);
}
