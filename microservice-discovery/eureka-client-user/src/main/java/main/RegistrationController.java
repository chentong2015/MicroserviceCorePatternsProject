package main;

import jakarta.annotation.Resource;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    // TODO. Spring Cloud提供的服务注册的登记信息
    @Resource
    Registration registration;

    // 获取当前服务所在的host主机的地址
    @GetMapping("/user/getIp")
    public String getTargetIp() {
        return "Address= " + this.registration.getHost() + ":" + this.registration.getPort();
    }
}
