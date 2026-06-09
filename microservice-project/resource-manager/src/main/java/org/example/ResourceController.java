package org.example;

import org.example.restclient.RestClientWrapper;
import org.example.restclient.RestRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class ResourceController {

    private final String microserviceName = "Messaging";
    private final String requestUrl = "/v1/notice";

    private final RestClientWrapper restClientWrapper;

    // TODO. 自动注入的RestTemplate在发送请求时自动负载均衡
    public ResourceController(RestTemplate restTemplate) {
          this.restClientWrapper = new RestClientWrapper(microserviceName, restTemplate);
    }

    @GetMapping("/lb")
    public String testLoadBalanced() {
        RestRequest restRequest = new RestRequest(requestUrl, null, HttpMethod.GET);
        final ResponseEntity<String> exchange = this.restClientWrapper.query(restRequest, new ParameterizedTypeReference<>() {});

        Optional<String> result = exchange.getStatusCode() == HttpStatus.OK ? Optional.ofNullable(exchange.getBody()) : Optional.empty();
        return "OK";
    }
}
