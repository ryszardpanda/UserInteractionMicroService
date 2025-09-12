package com.UserInteraction.UserInteractionMicroService.client.order;

import com.UserInteraction.UserInteractionMicroService.client.product.ProductMicroserviceConfiguration;
import feign.Logger;
import feign.QueryMapEncoder;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderMicroserviceConfiguration {

    @Bean
    public OrderErrorDecoder orderErrorDecoder() {
        return new OrderErrorDecoder();
    }

    @Bean
    public Retryer feignRetryerOrder() {
        return new Retryer.Default(100, 500, 10);
    }

    @Bean
    Logger.Level feignLoggerLevelForProduct() {
        return Logger.Level.FULL;
    }

    /**
     * Custom encoder dla Pageable - konwertuje Pageable na odpowiednie parametry query
     */
    public static class PageableQueryMapEncoder implements QueryMapEncoder {

        private final QueryMapEncoder delegate = new ProductMicroserviceConfiguration.FieldQueryMapEncoder();

        @Override
        public Map<String, Object> encode(Object object) {
            if (object instanceof Pageable) {
                return encodePageable((Pageable) object);
            }
            return delegate.encode(object);
        }

        private Map<String, Object> encodePageable(Pageable pageable) {
            Map<String, Object> params = new HashMap<>();

            params.put("page", pageable.getPageNumber());
            params.put("size", pageable.getPageSize());

            if (pageable.getSort().isSorted()) {
                List<String> sortParams = new ArrayList<>();
                pageable.getSort().forEach(order ->
                        sortParams.add(order.getProperty() + "," + order.getDirection().name().toLowerCase())
                );
                params.put("sort", sortParams.toArray(new String[0]));
            }

            return params;
        }
    }

    /**
     * Default field-based encoder jako fallback
     */
    public static class FieldQueryMapEncoder implements QueryMapEncoder {
        @Override
        public Map<String, Object> encode(Object object) {
            Map<String, Object> params = new HashMap<>();

            try {
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value != null) {
                        params.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to encode query map", e);
            }

            return params;
        }
    }
}
