package com.springcloud.openfeign.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/query")
public class MyDyFeignClientFactoryController {

    @Autowired
    DynamicFeignClientFactory<BaseRemoteApi> clientFactory;

    // 在获取FeignClient的时候通过FeignClientBuilder动态的设置service名称
    @RequestMapping("/userinfo")
    public String query(@RequestParam(value = "gid") String gid) {
        String service_location = "get correct service id";
        BaseRemoteApi api = clientFactory.getFeignClient(BaseRemoteApi.class, service_location);
        return api.info(gid);
    }

}
