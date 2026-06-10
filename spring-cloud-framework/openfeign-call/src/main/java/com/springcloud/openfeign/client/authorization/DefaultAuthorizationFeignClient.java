package com.springcloud.openfeign.client.authorization;

public class DefaultAuthorizationFeignClient implements DefaultAuthorizationClient {

    private final AuthorizationFeignClient feignClient;

    public DefaultAuthorizationFeignClient(AuthorizationFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    // 通过feignClient来调用指定的API进行身份的验证
    @Override
    public boolean checkPermission(String jwtToken, String resource, String action) {
        return feignClient.checkPermission("Bearer " + jwtToken, resource + action);
    }
}
