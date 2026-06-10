package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping("/v1/notice")
    public String notice() {
        System.out.println("call messaging microservice");
        return "Messaging notice";
    }
}
