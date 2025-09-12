package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.common.OrderStatus;
import com.UserInteraction.UserInteractionMicroService.dto.order.*;
import com.UserInteraction.UserInteractionMicroService.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderFacadeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getOrdersByUser_ValidRequest_ReturnsPageOfOrders() throws Exception {
        // given
        String userId = "user123";
        Pageable pageable = PageRequest.of(0, 10);

        OrderSummaryDTO orderSummary1 = new OrderSummaryDTO(
                1L,
                "ORD-001",
                BigDecimal.valueOf(99.99),
                LocalDateTime.of(2024, 1, 15, 10, 30)
        );

        OrderSummaryDTO orderSummary2 = new OrderSummaryDTO(
                2L,
                "ORD-002",
                BigDecimal.valueOf(149.99),
                LocalDateTime.of(2024, 1, 16, 14, 45)
        );

        Page<OrderSummaryDTO> orderPage = new PageImpl<>(
                List.of(orderSummary1, orderSummary2),
                pageable,
                2
        );

        when(orderFacadeService.getOrdersByUser(eq(userId), any(Pageable.class))).thenReturn(orderPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/order")
                        .param("userId", userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.content[0].totalGross").value(99.99))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].orderNumber").value("ORD-002"))
                .andExpect(jsonPath("$.content[1].totalGross").value(149.99))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));

        verify(orderFacadeService, times(1)).getOrdersByUser(eq(userId), any(Pageable.class));
    }

    @Test
    void getOrdersByUser_EmptyResult_ReturnsEmptyPage() throws Exception {
        // given
        String userId = "user456";
        Pageable pageable = PageRequest.of(0, 10);

        Page<OrderSummaryDTO> emptyPage = new PageImpl<>(
                Collections.emptyList(),
                pageable,
                0
        );

        when(orderFacadeService.getOrdersByUser(eq(userId), any(Pageable.class))).thenReturn(emptyPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/order")
                        .param("userId", userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.empty").value(true));

        verify(orderFacadeService, times(1)).getOrdersByUser(eq(userId), any(Pageable.class));
    }

    @Test
    void getOrdersByUser_WithPagination_ReturnsCorrectPage() throws Exception {
        // given
        String userId = "user789";
        Pageable pageable = PageRequest.of(1, 5);

        OrderSummaryDTO orderSummary = new OrderSummaryDTO(
                10L,
                "ORD-010",
                BigDecimal.valueOf(299.99),
                LocalDateTime.of(2024, 2, 1, 12, 0)
        );

        Page<OrderSummaryDTO> orderPage = new PageImpl<>(
                List.of(orderSummary),
                pageable,
                15
        );

        when(orderFacadeService.getOrdersByUser(eq(userId), any(Pageable.class))).thenReturn(orderPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/order")
                        .param("userId", userId)
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[0].orderNumber").value("ORD-010"))
                .andExpect(jsonPath("$.content[0].totalGross").value(299.99))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.totalPages").value(3));

        verify(orderFacadeService, times(1)).getOrdersByUser(eq(userId), any(Pageable.class));
    }

    @Test
    void placeOrder_ValidRequest_ReturnsOrderResponse() throws Exception {
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

        OrderResponseDTO orderResponse = new OrderResponseDTO(
                1L,
                "ORD-001",
                LocalDateTime.of(2024, 1, 15, 10, 30),
                OrderStatus.NEW,
                BigDecimal.valueOf(199.99),
                "PLN",
                Collections.emptyList()
        );

        when(orderFacadeService.placeOrder(eq(cartId), eq(userId), any(CreateOrderRequestDTO.class)))
                .thenReturn(orderResponse);

        // when & then
        mockMvc.perform(post("/api/user-interaction/order/cart/{cartId}/user/{userId}", cartId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.status").value("New"))
                .andExpect(jsonPath("$.totalGross").value(199.99))
                .andExpect(jsonPath("$.currency").value("PLN"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));

        verify(orderFacadeService, times(1)).placeOrder(eq(cartId), eq(userId), any(CreateOrderRequestDTO.class));
    }

    @Test
    void placeOrder_DifferentAddresses_ReturnsOrderResponse() throws Exception {
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

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setName("MacBook Pro");

        OrderResponseDTO orderResponse = new OrderResponseDTO(
                3L,
                "ORD-003",
                LocalDateTime.of(2024, 1, 17, 16, 20),
                OrderStatus.NEW,
                BigDecimal.valueOf(1299.99),
                "EUR",
                List.of(orderItemDTO)
        );

        when(orderFacadeService.placeOrder(eq(cartId), eq(userId), any(CreateOrderRequestDTO.class)))
                .thenReturn(orderResponse);

        // when & then
        mockMvc.perform(post("/api/user-interaction/order/cart/{cartId}/user/{userId}", cartId, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderNumber").value("ORD-003"))
                .andExpect(jsonPath("$.status").value("New"))
                .andExpect(jsonPath("$.totalGross").value(1299.99))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));

        verify(orderFacadeService, times(1)).placeOrder(eq(cartId), eq(userId), any(CreateOrderRequestDTO.class));
    }
}