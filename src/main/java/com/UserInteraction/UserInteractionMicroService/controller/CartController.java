package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.dto.cart.CartResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import com.UserInteraction.UserInteractionMicroService.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-interaction/cart")
@Slf4j
public class CartController {
    private final CartService cartFacadeService;

    @PostMapping("/{userId}/item")
    CartResponseDTO addItem(@PathVariable String userId, @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return cartFacadeService.addItem(userId, cartItemRequestDTO);
    }

    @DeleteMapping("/{cartId}/item/{cartItemId}")
    void removeItem(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartFacadeService.removeItem(cartId, cartItemId);
    }

    @DeleteMapping("/{id}")
    void deleteCart(@PathVariable Long id) {
        cartFacadeService.deleteCart(id);
    }

    @GetMapping("/{id}")
    CartResponseDTO getCartById(@PathVariable Long id) {
        return cartFacadeService.getCartById(id);
    }
}
