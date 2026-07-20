package testing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// TODO. @FeignClient URL
// 1. 初始化构建时必须设置url才能构建feign client否则抛异常“Failed to load ApplicationContext”
// 2. 当mock掉ProductService Feign Client，mock throw exception 则无需url
@FeignClient(value = "product-service", url = "http://localhost:5679/")
public interface ProductServiceFeignClient {

    // 和要发送请求的Service Controller的方法一致
    @PostMapping(value = "/products/{id}", consumes = "application/json;charset=UTF-8")
    ResponseEntity<String> insertProduct(@PathVariable("id") String id, @RequestBody Product product);

    // 方法可能抛出FeignException异常，被调用它的方法所捕获
    // 异常为被请求的server上的对于的错误信息(非200) !!
    @PostMapping(value = "/products/test/{id}", consumes = "application/json;charset=UTF-8")
    ResponseEntity<String> testInsertProduct(@PathVariable("id") String id, @RequestBody Product product);
}
