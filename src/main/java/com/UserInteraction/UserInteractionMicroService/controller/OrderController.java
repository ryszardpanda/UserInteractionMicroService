package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.dto.order.CreateOrderRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderSummaryDTO;
import com.UserInteraction.UserInteractionMicroService.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-interaction/order")
@Slf4j
public class OrderController {

    private final OrderService orderFacadeService;

    @GetMapping
    Page<OrderSummaryDTO> getOrdersByUser(@RequestParam String userId, @ParameterObject Pageable pageable){
        return orderFacadeService.getOrdersByUser(userId, pageable);
    }

    @PostMapping(value = "/cart/{cartId}/user/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    OrderResponseDTO placeOrder(@PathVariable Long cartId, @PathVariable String userId, @RequestBody CreateOrderRequestDTO body){
        return orderFacadeService.placeOrder(cartId, userId, body);
    }
}
