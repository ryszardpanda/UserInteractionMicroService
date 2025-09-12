package com.UserInteraction.UserInteractionMicroService.service;

import com.UserInteraction.UserInteractionMicroService.client.cart.CartMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.dto.cart.CartResponseDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemRequestDTO;
import com.UserInteraction.UserInteractionMicroService.dto.cart.cartItem.CartItemResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartMicroserviceClient cartMicroserviceClient;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        this.cartMicroserviceClient = Mockito.mock(CartMicroserviceClient.class);
        this.cartService = new CartService(cartMicroserviceClient);
    }

    @Test
    void addItem_ValidRequest_ReturnsCartResponse() {
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

        CartResponseDTO expectedResponse = new CartResponseDTO(
                1L,
                userId,
                List.of(cartItemResponse)
        );

        when(cartMicroserviceClient.addItem(userId, cartItemRequest)).thenReturn(expectedResponse);

        // when
        CartResponseDTO result = cartService.addItem(userId, cartItemRequest);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getCartId());
        assertEquals(userId, result.getUserId());
        assertEquals(1, result.getItems().size());
        assertEquals("Laptop Dell", result.getItems().get(0).getName());
        assertEquals(2, result.getItems().get(0).getQuantity());

        verify(cartMicroserviceClient, times(1)).addItem(eq(userId), eq(cartItemRequest));
    }

    @Test
    void addItem_NewCart_CreatesNewCart() {
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

        CartResponseDTO expectedResponse = new CartResponseDTO(
                2L,
                userId,
                List.of()
        );

        when(cartMicroserviceClient.addItem(userId, cartItemRequest)).thenReturn(expectedResponse);

        // when
        CartResponseDTO result = cartService.addItem(userId, cartItemRequest);

        // then
        assertNotNull(result);
        assertEquals(2L, result.getCartId());
        assertEquals(userId, result.getUserId());

        verify(cartMicroserviceClient, times(1)).addItem(eq(userId), eq(cartItemRequest));
    }

    @Test
    void removeItem_ValidIds_CallsClientRemoveItem() {
        // given
        Long cartId = 1L;
        Long cartItemId = 5L;

        // when
        cartService.removeItem(cartId, cartItemId);

        // then
        verify(cartMicroserviceClient, times(1)).removeItem(eq(cartId), eq(cartItemId));
    }

    @Test
    void deleteCart_ValidId_CallsClientDeleteCart() {
        // given
        Long cartId = 1L;

        // when
        cartService.deleteCart(cartId);

        // then
        verify(cartMicroserviceClient, times(1)).deleteCart(eq(cartId));
    }

    @Test
    void getCartById_ValidId_ReturnsCartResponse() {
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

        CartResponseDTO expectedResponse = new CartResponseDTO(
                cartId,
                "user123",
                List.of(item1, item2)
        );

        when(cartMicroserviceClient.getCartById(cartId)).thenReturn(expectedResponse);

        // when
        CartResponseDTO result = cartService.getCartById(cartId);

        // then
        assertNotNull(result);
        assertEquals(cartId, result.getCartId());
        assertEquals("user123", result.getUserId());
        assertEquals(2, result.getItems().size());
        assertEquals("Laptop", result.getItems().get(0).getName());
        assertEquals("Mouse", result.getItems().get(1).getName());
        assertEquals(1, result.getItems().get(0).getQuantity());
        assertEquals(2, result.getItems().get(1).getQuantity());

        verify(cartMicroserviceClient, times(1)).getCartById(eq(cartId));
    }

    @Test
    void getCartById_EmptyCart_ReturnsEmptyCartResponse() {
        // given
        Long cartId = 2L;

        CartResponseDTO expectedResponse = new CartResponseDTO(
                cartId,
                "user456",
                List.of()
        );

        when(cartMicroserviceClient.getCartById(cartId)).thenReturn(expectedResponse);

        // when
        CartResponseDTO result = cartService.getCartById(cartId);

        // then
        assertNotNull(result);
        assertEquals(cartId, result.getCartId());
        assertEquals("user456", result.getUserId());
        assertTrue(result.getItems().isEmpty());

        verify(cartMicroserviceClient, times(1)).getCartById(eq(cartId));
    }
}