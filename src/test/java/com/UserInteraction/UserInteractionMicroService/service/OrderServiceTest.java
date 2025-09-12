package com.UserInteraction.UserInteractionMicroService.service;

import com.UserInteraction.UserInteractionMicroService.client.order.OrderMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.common.OrderStatus;
import com.UserInteraction.UserInteractionMicroService.dto.order.CreateOrderRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderAddressDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.order.OrderSummaryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private OrderMicroserviceClient orderMicroserviceClient;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.orderMicroserviceClient = Mockito.mock(OrderMicroserviceClient.class);
        this.orderService = new OrderService(orderMicroserviceClient);
    }

    @Test
    void getOrdersByUser_ValidRequest_ReturnsPageOfOrders() {
        // given
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);

        OrderSummaryDTO orderSummary1 = new OrderSummaryDTO(
                1L,
                "ORD-001",
                BigDecimal.valueOf(99.99),
                LocalDateTime.now()
        );

        OrderSummaryDTO orderSummary2 = new OrderSummaryDTO(
                2L,
                "ORD-002",
                BigDecimal.valueOf(149.99),
                LocalDateTime.now()
        );

        Page<OrderSummaryDTO> expectedPage = new PageImpl<>(
                List.of(orderSummary1, orderSummary2),
                pageable,
                2
        );

        when(orderMicroserviceClient.getOrdersByUser(userId, pageable)).thenReturn(expectedPage);

        // when
        Page<OrderSummaryDTO> result = orderService.getOrdersByUser(userId, pageable);

        // then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals("ORD-001", result.getContent().get(0).getOrderNumber());
        assertEquals("ORD-002", result.getContent().get(1).getOrderNumber());
        assertEquals(BigDecimal.valueOf(99.99), result.getContent().get(0).getTotalGross());
        assertEquals(BigDecimal.valueOf(149.99), result.getContent().get(1).getTotalGross());

        verify(orderMicroserviceClient, times(1)).getOrdersByUser(eq(userId), eq(pageable));
    }

    @Test
    void getOrdersByUser_EmptyResult_ReturnsEmptyPage() {
        // given
        String userId = "user456";
        Pageable pageable = PageRequest.of(0, 10);

        Page<OrderSummaryDTO> emptyPage = new PageImpl<>(
                Collections.emptyList(),
                pageable,
                0
        );

        when(orderMicroserviceClient.getOrdersByUser(userId, pageable)).thenReturn(emptyPage);

        // when
        Page<OrderSummaryDTO> result = orderService.getOrdersByUser(userId, pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(orderMicroserviceClient, times(1)).getOrdersByUser(eq(userId), eq(pageable));
    }

    @Test
    void getOrdersByUser_FirstPage_ReturnsCorrectPageInfo() {
        // given
        String userId = "user789";
        Pageable pageable = PageRequest.of(0, 5);

        OrderSummaryDTO orderSummary = new OrderSummaryDTO(
                1L,
                "ORD-001",
                BigDecimal.valueOf(299.99),
                LocalDateTime.now()
        );

        Page<OrderSummaryDTO> expectedPage = new PageImpl<>(
                List.of(orderSummary),
                pageable,
                1
        );

        when(orderMicroserviceClient.getOrdersByUser(userId, pageable)).thenReturn(expectedPage);

        // when
        Page<OrderSummaryDTO> result = orderService.getOrdersByUser(userId, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(5, result.getSize());

        verify(orderMicroserviceClient, times(1)).getOrdersByUser(eq(userId), eq(pageable));
    }

    @Test
    void placeOrder_ValidRequest_ReturnsOrderResponse() {
        // given
        Long cartId = 123L;
        String userId = "user123";

        OrderAddressDTO shippingAddress = new OrderAddressDTO(
                "Jan",
                "Kowalski",
                "ul. Marszałkowska 1",
                "Warszawa",
                "00-001",
                "+48123456789"
        );

        OrderAddressDTO billingAddress = new OrderAddressDTO(
                "Jan",
                "Kowalski",
                "ul. Marszałkowska 1",
                "Warszawa",
                "00-001",
                "+48123456789"
        );

        CreateOrderRequestDTO orderRequest = new CreateOrderRequestDTO(
                shippingAddress,
                billingAddress
        );

        OrderResponseDTO expectedResponse = new OrderResponseDTO(
                1L,
                "ORD-001",
                LocalDateTime.now(),
                OrderStatus.NEW,
                BigDecimal.valueOf(199.99),
                "PLN",
                Collections.emptyList()
        );

        when(orderMicroserviceClient.placeOrder(cartId, userId, orderRequest)).thenReturn(expectedResponse);

        // when
        OrderResponseDTO result = orderService.placeOrder(cartId, userId, orderRequest);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ORD-001", result.getOrderNumber());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals(BigDecimal.valueOf(199.99), result.getTotalGross());
        assertEquals("PLN", result.getCurrency());
        assertTrue(result.getItems().isEmpty());

        verify(orderMicroserviceClient, times(1)).placeOrder(eq(cartId), eq(userId), eq(orderRequest));
    }


    @Test
    void placeOrder_DifferentAddresses_ReturnsOrderResponse() {
        // given
        Long cartId = 789L;
        String userId = "user789";

        OrderAddressDTO shippingAddress = new OrderAddressDTO(
                "Piotr",
                "Wiśniewski",
                "ul. Gdańska 5",
                "Gdańsk",
                "80-001",
                "+48111222333"
        );

        OrderAddressDTO billingAddress = new OrderAddressDTO(
                "Piotr",
                "Wiśniewski",
                "ul. Poznańska 15",
                "Poznań",
                "60-001",
                "+48111222333"
        );

        CreateOrderRequestDTO orderRequest = new CreateOrderRequestDTO(
                shippingAddress,
                billingAddress
        );

        OrderResponseDTO expectedResponse = new OrderResponseDTO(
                3L,
                "ORD-003",
                LocalDateTime.now(),
                OrderStatus.NEW,
                BigDecimal.valueOf(1299.99),
                "EUR",
                Collections.emptyList()
        );

        when(orderMicroserviceClient.placeOrder(cartId, userId, orderRequest)).thenReturn(expectedResponse);

        // when
        OrderResponseDTO result = orderService.placeOrder(cartId, userId, orderRequest);

        // then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("ORD-003", result.getOrderNumber());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals(BigDecimal.valueOf(1299.99), result.getTotalGross());
        assertEquals("EUR", result.getCurrency());
        assertEquals("Gdańsk", shippingAddress.getCity());
        assertEquals("Poznań", billingAddress.getCity());

        verify(orderMicroserviceClient, times(1)).placeOrder(eq(cartId), eq(userId), eq(orderRequest));
    }
}