package com.UserInteraction.UserInteractionMicroService.dto.product;

import com.UserInteraction.UserInteractionMicroService.common.ProductsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String name;
    private BigDecimal price;
    private ProductsType type;
    private int quantity;
    private List<ProductConfigurationDTO> configurations;
}

