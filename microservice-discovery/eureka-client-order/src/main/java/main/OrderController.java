package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    //TODO. 通过"微服务名"来从注册中心发现特定的服务，RestTemplate会负载均衡调用
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/order/{id}")
    public String getOrderById(@PathVariable("id") int id) {
        String user = restTemplate.getForObject("http://user-service/user/" + id, String.class);
        return "Order with user:" + user;
    }

    @GetMapping("/order/getTargetIp")
    public String getTargetIp() {
        String ip = restTemplate.getForObject("http://user-service/user/getIp", String.class);
        return "Target IP:" + ip;
    }
}
