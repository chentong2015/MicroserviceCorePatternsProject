package main;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderClientController {

    // TODO. 使用EurekaClient获取注册的服务名称(负载均衡到多个Services)
    @Autowired
    private EurekaClient discoveryClient;

    @GetMapping("/order/getNextIp")
    public String getNextIp() {
        InstanceInfo instance = this.discoveryClient.getNextServerFromEureka("user-service", false);
        return instance.getHomePageUrl();
    }
}
