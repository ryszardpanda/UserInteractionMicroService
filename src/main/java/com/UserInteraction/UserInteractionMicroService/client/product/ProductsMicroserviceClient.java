package com.UserInteraction.UserInteractionMicroService.client.product;

import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "products-microservice", url = "${base.url.products-microservice}", configuration = ProductMicroserviceConfiguration.class )
public interface ProductsMicroserviceClient {

    @GetMapping()
    Page<ProductDTO> getProducts(@SpringQueryMap Pageable pageable);

    @GetMapping("/{id}")
    ProductDTO getProductById(@PathVariable Long id);
}