package com.UserInteraction.UserInteractionMicroService.client.cart;


import com.UserInteraction.UserInteractionMicroService.dto.cart.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cart-microservice", url = "${base.url.cart-microservice}")
public interface CartMicroserviceClient {
    @GetMapping("/{id}")
    CartDTO getCartById(@PathVariable Long id);
}
