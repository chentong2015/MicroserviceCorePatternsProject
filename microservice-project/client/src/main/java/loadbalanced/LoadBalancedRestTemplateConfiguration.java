package loadbalanced;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancedRestTemplateConfiguration {

    @LoadBalanced
    @Primary
    @Bean({"basicLoadBalancedRestTemplate"})
    public RestTemplate basicLoadBalancedRestTemplate() {
        return new RestTemplate();
    }
}
