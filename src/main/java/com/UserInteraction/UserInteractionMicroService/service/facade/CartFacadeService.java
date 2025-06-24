package com.UserInteraction.UserInteractionMicroService.service.facade;

import com.UserInteraction.UserInteractionMicroService.client.cart.CartMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.dto.cart.CartResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartFacadeService {

    private final CartMicroserviceClient cartMicroserviceClient;

    public CartResponseDTO addItem(String userId, CartItemRequestDTO cartItemRequestDTO) {
        return cartMicroserviceClient.addItem(userId, cartItemRequestDTO);
    }

    public void removeItem(Long cartId, Long cartItemId) {
        cartMicroserviceClient.removeItem(cartId, cartItemId);
    }

    public void deleteCart(Long id) {
        cartMicroserviceClient.deleteCart(id);
    }

    public CartResponseDTO getCartById(Long id) {
        return cartMicroserviceClient.getCartById(id);
    }
}
