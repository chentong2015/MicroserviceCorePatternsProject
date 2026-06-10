package loadbalanced;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO. 使用注解来激活RestTemplate负载均衡请求, 客户端自定义配置
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({LoadBalancedRestTemplateConfiguration.class})
public @interface EnableLoadBalancedBasicRestTemplate {
}
