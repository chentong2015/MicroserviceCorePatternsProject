package com.springcloud.openfeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// fallback: a default code path that is executed when the circuit is open or there is an error.
// 1. set the fallback attribute to the class name that implements the fallback.
// 2. declare your implementation as a Spring bean
@FeignClient(name = "test", url = "http://localhost:${server.port}/", fallback = MyCallback.class)
public interface MyCallbackInterface {

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    String getHello();

    @RequestMapping(method = RequestMethod.GET, value = "/hellonotfound")
    String getException();
}
