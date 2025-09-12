package com.UserInteraction.UserInteractionMicroService.dto.product;

import com.UserInteraction.UserInteractionMicroService.common.ConfigurationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigurationDTO {
    private String name;
    private BigDecimal price;
    private ConfigurationType type;
}
