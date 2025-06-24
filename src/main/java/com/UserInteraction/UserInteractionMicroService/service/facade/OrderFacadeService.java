package com.UserInteraction.UserInteractionMicroService.service.facade;

import com.UserInteraction.UserInteractionMicroService.client.order.OrderMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.dto.order.CreateOrderRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class OrderFacadeService {
    private final OrderMicroserviceClient orderMicroserviceClient;

    public Page<OrderSummaryDTO> getOrdersByUser(@RequestParam String userId, @ParameterObject Pageable pageable){
        return orderMicroserviceClient.getOrdersByUser(userId, pageable);
    }

    public OrderResponseDTO placeOrder(@PathVariable Long cartId, @PathVariable String userId, @RequestBody CreateOrderRequestDTO body){
        return orderMicroserviceClient.placeOrder(cartId, userId, body);
    }
}
