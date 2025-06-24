package com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem;

import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.cartItemConfiguration.CartItemConfigurationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {
    private Long id;
    private Long productId;
    private List<CartItemConfigurationRequestDTO> configurations = new ArrayList<>();
    private String name;
    private BigDecimal price;
    private int quantity;
}
