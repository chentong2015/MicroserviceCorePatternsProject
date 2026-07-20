package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/fallback")
    public String circuitBreakerFallback() {
        return "This is a fallback";
    }
}
