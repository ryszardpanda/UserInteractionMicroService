package com.UserInteraction.UserInteractionMicroService.client.cart;


import com.UserInteraction.UserInteractionMicroService.dto.cart.CartResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "cart-microservice", url = "${base.url.cart-microservice}", configuration = CartMicroserviceConfiguration.class)
public interface CartMicroserviceClient {

    @GetMapping("/{id}")
    CartResponseDTO getCartById(@PathVariable Long id);

    @PostMapping("/{userId}/item")
    CartResponseDTO addItem(@PathVariable String userId, @RequestBody CartItemRequestDTO cartItemRequestDTO);

    @DeleteMapping("/{cartId}/item/{cartItemId}")
    void removeItem(@PathVariable Long cartId, @PathVariable Long cartItemId);

    @DeleteMapping("/{id}")
    void deleteCart(@PathVariable Long id);
}