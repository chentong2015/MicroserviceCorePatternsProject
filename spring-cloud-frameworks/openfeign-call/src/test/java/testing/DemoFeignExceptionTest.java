package testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import org.junit.Assert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.core.IsInstanceOf.any;
import static org.mockito.ArgumentMatchers.anyString;

public class DemoFeignExceptionTest {
    
    public void testMockFeignClient() throws IOException {
        Client client = Mockito.mock(Client.class);
        ProductServiceFeignClient productServiceFeignClient = Feign.builder()
                .client(client)
                .target(ProductServiceFeignClient.class, "http://testing-url");

        // TODO. mock掉Feign Client & Response
        Request request = Request.create(Request.HttpMethod.POST, "http://testing-url",
                Collections.emptyMap(), null, Util.UTF_8);
        Response response = Response.builder()
                .headers(Collections.emptyMap())
                .request(request)
                .status(200)
                .build();
        Mockito.when(client.execute(Mockito.any(Request.class), Mockito.any(Request.Options.class)))
                .thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        Product product = new Product();
        String jsonBody = mapper.writeValueAsString(product);
        productServiceFeignClient.insertProduct("id", product);

        ArgumentCaptor<Request> requestArgumentCaptor = ArgumentCaptor.forClass(Request.class);
        Mockito.verify(client).execute(requestArgumentCaptor.capture(), Mockito.any(Request.Options.class));
        Request request1 = requestArgumentCaptor.getValue();
        Assert.assertEquals(
                "Unexpected http method used.",
                "POST",
                request1.method()
        );
    }

    // 标准mock FeignException异常的抛出
    public void testMockFeignResponse() {
        Client client = Mockito.mock(Client.class);
        ProductServiceFeignClient productService = Feign.builder()
                .client(client)
                .target(ProductServiceFeignClient.class, "http://testing-url");

        Response response = Response.builder()
               .status(400)
               .reason("bad request body")
               .headers(Collections.emptyMap())
               .request(Request.create(Request.HttpMethod.POST, "/products/test/2", Collections.emptyMap(), null, Util.UTF_8))
               .body("Product already exists", StandardCharsets.UTF_8)
               .build();

        Mockito.doThrow(FeignException.errorStatus("method key test", response))
                 .when(productService).testInsertProduct(anyString(), (Product) any(Product.class));
    }
}
