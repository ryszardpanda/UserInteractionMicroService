package com.UserInteraction.UserInteractionMicroService.client.product;

import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "products-microservice", url = "${base.url.products-microservice}")
public interface ProductsMicroserviceClient {

    @GetMapping()
    Page<ProductDTO> getProducts(@RequestParam("page") int page, @RequestParam("size") int size);

    default Page<ProductDTO> getProducts() {
        return getProducts(0, 20);
    }

    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable Long id);
}