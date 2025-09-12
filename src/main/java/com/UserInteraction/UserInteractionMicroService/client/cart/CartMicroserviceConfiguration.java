package com.UserInteraction.UserInteractionMicroService.client.cart;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

public class CartMicroserviceConfiguration {

    @Bean
    public CartErrorDecoder cartErrorDecoder() {
        return new CartErrorDecoder();
    }

    @Bean
    public Retryer feignRetryerCart(){
        return new Retryer.Default(100, 500, 10);
    }

    @Bean
    Logger.Level feignLoggerLevelForCart() {
        return Logger.Level.FULL;
    }
}
