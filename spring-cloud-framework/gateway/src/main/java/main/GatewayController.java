package main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/circuit-breaker-fallback")
    public String circuitBreakerFallback() {
        return "This is a fallback";
    }
}
