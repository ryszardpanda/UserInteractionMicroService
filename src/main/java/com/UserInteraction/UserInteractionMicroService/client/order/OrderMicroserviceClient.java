package com.UserInteraction.UserInteractionMicroService.client.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "order-microservice", url = "${base.url.order-microservice}",  configuration = OrderMicroserviceConfiguration.class)
public interface OrderMicroserviceClient {
}
