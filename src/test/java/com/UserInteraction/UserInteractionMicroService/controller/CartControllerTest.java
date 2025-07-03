package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.dto.cart.CartResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemResponseDTO;
import com.UserInteraction.UserInteractionMicroService.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem_ValidRequest_ReturnsCartResponse() throws Exception {
        // given
        String userId = "user123";
        CartItemRequestDTO cartItemRequest = new CartItemRequestDTO(
                1L,
                100L,
                List.of(),
                "Laptop Dell",
                BigDecimal.valueOf(2500.00),
                2
        );

        CartItemResponseDTO cartItemResponse = new CartItemResponseDTO();
        cartItemResponse.setCartItemId(1L);
        cartItemResponse.setName("Laptop Dell");
        cartItemResponse.setQuantity(2);

        CartResponseDTO cartResponse = new CartResponseDTO(
                1L,
                userId,
                List.of(cartItemResponse)
        );

        when(cartService.addItem(eq(userId), any(CartItemRequestDTO.class))).thenReturn(cartResponse);

        // when & then
        mockMvc.perform(post("/api/user-interaction/cart/{userId}/item", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].name").value("Laptop Dell"))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        verify(cartService, times(1)).addItem(eq(userId), any(CartItemRequestDTO.class));
    }

    @Test
    void addItem_NewCart_ReturnsNewCartResponse() throws Exception {
        // given
        String userId = "newUser456";
        CartItemRequestDTO cartItemRequest = new CartItemRequestDTO(
                null,
                200L,
                List.of(),
                "iPhone 15",
                BigDecimal.valueOf(3500.00),
                1
        );

        CartResponseDTO cartResponse = new CartResponseDTO(
                2L,
                userId,
                List.of()
        );

        when(cartService.addItem(eq(userId), any(CartItemRequestDTO.class))).thenReturn(cartResponse);

        // when & then
        mockMvc.perform(post("/api/user-interaction/cart/{userId}/item", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(2))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));

        verify(cartService, times(1)).addItem(eq(userId), any(CartItemRequestDTO.class));
    }

    @Test
    void removeItem_ValidIds_Returns200() throws Exception {
        // given
        Long cartId = 1L;
        Long cartItemId = 5L;

        // when & then
        mockMvc.perform(delete("/api/user-interaction/cart/{cartId}/item/{cartItemId}", cartId, cartItemId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeItem(eq(cartId), eq(cartItemId));
    }

    @Test
    void deleteCart_ValidId_Returns200() throws Exception {
        // given
        Long cartId = 1L;

        // when & then
        mockMvc.perform(delete("/api/user-interaction/cart/{id}", cartId))
                .andExpect(status().isOk());

        verify(cartService, times(1)).deleteCart(eq(cartId));
    }

    @Test
    void getCartById_ValidId_ReturnsCartResponse() throws Exception {
        // given
        Long cartId = 1L;

        CartItemResponseDTO item1 = new CartItemResponseDTO();
        item1.setCartItemId(1L);
        item1.setName("Laptop");
        item1.setQuantity(1);

        CartItemResponseDTO item2 = new CartItemResponseDTO();
        item2.setCartItemId(2L);
        item2.setName("Mouse");
        item2.setQuantity(2);

        CartResponseDTO cartResponse = new CartResponseDTO(
                cartId,
                "user123",
                List.of(item1, item2)
        );

        when(cartService.getCartById(cartId)).thenReturn(cartResponse);

        // when & then
        mockMvc.perform(get("/api/user-interaction/cart/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(cartId))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].name").value("Laptop"))
                .andExpect(jsonPath("$.items[0].quantity").value(1))
                .andExpect(jsonPath("$.items[1].name").value("Mouse"))
                .andExpect(jsonPath("$.items[1].quantity").value(2));

        verify(cartService, times(1)).getCartById(eq(cartId));
    }

    @Test
    void getCartById_EmptyCart_ReturnsEmptyCartResponse() throws Exception {
        // given
        Long cartId = 2L;

        CartResponseDTO cartResponse = new CartResponseDTO(
                cartId,
                "user456",
                List.of()
        );

        when(cartService.getCartById(cartId)).thenReturn(cartResponse);

        // when & then
        mockMvc.perform(get("/api/user-interaction/cart/{id}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cartId").value(cartId))
                .andExpect(jsonPath("$.userId").value("user456"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(0));

        verify(cartService, times(1)).getCartById(eq(cartId));
    }
}