package com.UserInteraction.UserInteractionMicroService.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Long id;
    private String orderNumber;
    private BigDecimal totalGross;
    private LocalDateTime createdAt;
}
