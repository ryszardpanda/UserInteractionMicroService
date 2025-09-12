package com.UserInteraction.UserInteractionMicroService.dto.cart;

import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private Long cartId;
    private String userId;
    private List<CartItemResponseDTO> items = new ArrayList<>();
}
