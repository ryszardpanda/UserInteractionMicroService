package com.UserInteraction.UserInteractionMicroService.client.product;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductMicroserviceConfiguration {
//    @Bean
//    public ErrorDecoder productFetcherErrorDecoder(){
//        return new ProductErrorDecoder();
//    }

    @Bean
    public Retryer feignRetryerProduct(){
        return new Retryer.Default(100, 500, 10);
    }

    @Bean
    Logger.Level feignLoggerLevelForProduct() {
        return Logger.Level.FULL;
    }
}
