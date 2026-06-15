package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    // 测试微服务之间的调用, 相互通讯
    @GetMapping("/v1/notice")
    public String notice() {
        System.out.println("call messaging microservice");
        return "Messaging notice";
    }
}
