package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.common.ProductsType;
import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import com.UserInteraction.UserInteractionMicroService.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getProducts_ValidRequest_ReturnsProductsPage() throws Exception {
        // given
        ProductDTO product1 = new ProductDTO("Laptop Dell", BigDecimal.valueOf(2500.00), ProductsType.COMPUTER, 10, List.of());
        ProductDTO product2 = new ProductDTO("iPhone 15", BigDecimal.valueOf(3500.00), ProductsType.SMARTPHONE, 15, List.of());

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> productsPage = new PageImpl<>(List.of(product1, product2), pageable, 2);

        when(productService.getProducts(any(Pageable.class))).thenReturn(productsPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Laptop Dell"))
                .andExpect(jsonPath("$.content[0].price").value(2500.00))
                .andExpect(jsonPath("$.content[0].type").value("Computer"))
                .andExpect(jsonPath("$.content[1].name").value("iPhone 15"))
                .andExpect(jsonPath("$.content[1].price").value(3500.00))
                .andExpect(jsonPath("$.content[1].type").value("Smartphone"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getProducts_EmptyResult_ReturnsEmptyPage() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(productService.getProducts(any(Pageable.class))).thenReturn(emptyPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getProductsByType_ValidTypeComputer_ReturnsComputersPage() throws Exception {
        // given
        ProductDTO laptop = new ProductDTO("Laptop Lenovo", BigDecimal.valueOf(2200.00), ProductsType.COMPUTER, 8, List.of());
        ProductDTO desktop = new ProductDTO("Desktop HP", BigDecimal.valueOf(1800.00), ProductsType.COMPUTER, 5, List.of());

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> computersPage = new PageImpl<>(List.of(laptop, desktop), pageable, 2);

        when(productService.getProductsByType(eq(ProductsType.COMPUTER), any(Pageable.class)))
                .thenReturn(computersPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/products/by-type")
                        .param("type", "COMPUTER")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Laptop Lenovo"))
                .andExpect(jsonPath("$.content[0].type").value("Computer"))
                .andExpect(jsonPath("$.content[1].name").value("Desktop HP"))
                .andExpect(jsonPath("$.content[1].type").value("Computer"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void getProductsByType_ValidTypeSmartphone_ReturnsSmartphonesPage() throws Exception {
        // given
        ProductDTO phone1 = new ProductDTO("Samsung Galaxy S24", BigDecimal.valueOf(3200.00), ProductsType.SMARTPHONE, 12, List.of());
        ProductDTO phone2 = new ProductDTO("iPhone 14", BigDecimal.valueOf(2900.00), ProductsType.SMARTPHONE, 7, List.of());

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> smartphonesPage = new PageImpl<>(List.of(phone1, phone2), pageable, 2);

        when(productService.getProductsByType(eq(ProductsType.SMARTPHONE), any(Pageable.class)))
                .thenReturn(smartphonesPage);

        // when & then
        mockMvc.perform(get("/api/user-interaction/products/by-type")
                        .param("type", "SMARTPHONE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("Samsung Galaxy S24"))
                .andExpect(jsonPath("$.content[0].type").value("Smartphone"))
                .andExpect(jsonPath("$.content[1].name").value("iPhone 14"))
                .andExpect(jsonPath("$.content[1].type").value("Smartphone"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}