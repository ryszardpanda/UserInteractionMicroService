package com.UserInteraction.UserInteractionMicroService.client.order;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

public class OrderMicroserviceConfiguration {
    @Bean
    public Retryer feignRetryerOrder(){
        return new Retryer.Default(100, 500, 10);
    }

    @Bean
    Logger.Level feignLoggerLevelForProduct() {
        return Logger.Level.FULL;
    }
}
