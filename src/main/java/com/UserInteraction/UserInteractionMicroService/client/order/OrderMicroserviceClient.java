package com.UserInteraction.UserInteractionMicroService.client.order;

import com.UserInteraction.UserInteractionMicroService.dto.order.CreateOrderRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderSummaryDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "order-microservice", url = "${base.url.order-microservice}",  configuration = OrderMicroserviceConfiguration.class)
public interface OrderMicroserviceClient {

    @GetMapping
    Page<OrderSummaryDTO> getOrdersByUser(@RequestParam String userId, @ParameterObject Pageable pageable);

    @PostMapping(value = "/cart/{cartId}/user/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    OrderResponseDTO placeOrder(@PathVariable Long cartId, @PathVariable String userId, @RequestBody CreateOrderRequestDTO body);

}
