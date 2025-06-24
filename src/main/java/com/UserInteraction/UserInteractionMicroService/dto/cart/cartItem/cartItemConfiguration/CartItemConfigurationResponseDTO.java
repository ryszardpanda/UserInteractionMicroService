package com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.cartItemConfiguration;

import com.UserInteraction.UserInteractionMicroService.common.ProductsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemConfigurationResponseDTO {
    private Long cartItemConfigurationId;
    private String name;
    private BigDecimal price;
    private ProductsType type;
}
