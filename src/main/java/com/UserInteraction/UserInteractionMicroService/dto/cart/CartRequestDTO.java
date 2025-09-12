package com.UserInteraction.UserInteractionMicroService.dto.cart;

import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    private String userId;
    private List<CartItemRequestDTO> items = new ArrayList<>();
}
