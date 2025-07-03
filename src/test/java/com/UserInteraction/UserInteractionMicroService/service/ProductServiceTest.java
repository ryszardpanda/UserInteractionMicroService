package com.UserInteraction.UserInteractionMicroService.service;

import com.UserInteraction.UserInteractionMicroService.client.product.ProductsMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.common.ProductsType;
import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    private ProductsMicroserviceClient productsMicroserviceClient;
    private ProductService productService;

    @BeforeEach
    void setUp(){
        this.productsMicroserviceClient = Mockito.mock(ProductsMicroserviceClient.class);
        this.productService = new ProductService(productsMicroserviceClient);
    }

    @Test
    void getProducts_ProductsExist_ProductsPageReturned(){
        // given
        ProductDTO product1 = new ProductDTO("Laptop Dell", BigDecimal.valueOf(2500.00), ProductsType.COMPUTER, 10, List.of());
        ProductDTO product2 = new ProductDTO("iPhone 15", BigDecimal.valueOf(3500.00), ProductsType.SMARTPHONE, 15, List.of());

        Pageable pageReq = PageRequest.of(0, 2, Sort.by("price").descending());
        PageImpl<ProductDTO> expectedPage = new PageImpl<>(List.of(product1, product2), pageReq, 2);

        when(productsMicroserviceClient.getProducts(pageReq)).thenReturn(expectedPage);

        // when
        Page<ProductDTO> result = productService.getProducts(pageReq);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Laptop Dell", result.getContent().get(0).getName());
        assertEquals("iPhone 15", result.getContent().get(1).getName());
        assertEquals(BigDecimal.valueOf(2500.00), result.getContent().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(3500.00), result.getContent().get(1).getPrice());
        assertEquals(ProductsType.COMPUTER, result.getContent().get(0).getType());
        assertEquals(ProductsType.SMARTPHONE, result.getContent().get(1).getType());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());

        verify(productsMicroserviceClient, times(1)).getProducts(pageReq);
    }

    @Test
    void getProducts_EmptyResult_EmptyPageReturned(){
        // given
        Pageable pageReq = PageRequest.of(0, 10);
        PageImpl<ProductDTO> emptyPage = new PageImpl<>(List.of(), pageReq, 0);

        when(productsMicroserviceClient.getProducts(pageReq)).thenReturn(emptyPage);

        // when
        Page<ProductDTO> result = productService.getProducts(pageReq);

        // then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

        verify(productsMicroserviceClient, times(1)).getProducts(pageReq);
    }

    @Test
    void getProductsByType_ComputersExist_ComputersPageReturned(){
        // given
        ProductDTO laptop = new ProductDTO("Laptop Lenovo", BigDecimal.valueOf(2200.00), ProductsType.COMPUTER, 8, List.of());
        ProductDTO desktop = new ProductDTO("Desktop HP", BigDecimal.valueOf(1800.00), ProductsType.COMPUTER, 5, List.of());

        ProductsType type = ProductsType.COMPUTER;
        Pageable pageReq = PageRequest.of(0, 10, Sort.by("name").ascending());
        PageImpl<ProductDTO> expectedPage = new PageImpl<>(List.of(laptop, desktop), pageReq, 2);

        when(productsMicroserviceClient.getProductByType(type, pageReq)).thenReturn(expectedPage);

        // when
        Page<ProductDTO> result = productService.getProductsByType(type, pageReq);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        // Sprawdzamy czy wszystkie produkty są typu COMPUTER
        result.getContent().forEach(product -> assertEquals(ProductsType.COMPUTER, product.getType()));

        assertEquals("Laptop Lenovo", result.getContent().get(0).getName());
        assertEquals("Desktop HP", result.getContent().get(1).getName());
        assertEquals(BigDecimal.valueOf(2200.00), result.getContent().get(0).getPrice());
        assertEquals(BigDecimal.valueOf(1800.00), result.getContent().get(1).getPrice());
        assertEquals(8, result.getContent().get(0).getQuantity());
        assertEquals(5, result.getContent().get(1).getQuantity());

        verify(productsMicroserviceClient, times(1)).getProductByType(eq(type), eq(pageReq));
    }

    @Test
    void getProductsByType_PhonesExist_PhonesPageReturned(){
        // given
        ProductDTO phone1 = new ProductDTO("Samsung Galaxy S24", BigDecimal.valueOf(3200.00), ProductsType.SMARTPHONE, 12, List.of());
        ProductDTO phone2 = new ProductDTO("iPhone 14", BigDecimal.valueOf(2900.00), ProductsType.SMARTPHONE, 7, List.of());

        ProductsType type = ProductsType.SMARTPHONE;
        Pageable pageReq = PageRequest.of(0, 5, Sort.by("price").descending());
        PageImpl<ProductDTO> expectedPage = new PageImpl<>(List.of(phone1, phone2), pageReq, 2);

        when(productsMicroserviceClient.getProductByType(type, pageReq)).thenReturn(expectedPage);

        // when
        Page<ProductDTO> result = productService.getProductsByType(type, pageReq);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        result.getContent().forEach(product -> assertEquals(ProductsType.SMARTPHONE, product.getType()));

        assertEquals("Samsung Galaxy S24", result.getContent().get(0).getName());
        assertEquals("iPhone 14", result.getContent().get(1).getName());
        assertEquals(0, result.getNumber());
        assertEquals(5, result.getSize());

        verify(productsMicroserviceClient, times(1)).getProductByType(eq(type), eq(pageReq));
    }

    @Test
    void getProductsByType_NoProductsOfType_EmptyPageReturned(){
        // given
        ProductsType type = ProductsType.COMPUTER;
        Pageable pageReq = PageRequest.of(0, 10);
        PageImpl<ProductDTO> emptyPage = new PageImpl<>(List.of(), pageReq, 0);

        when(productsMicroserviceClient.getProductByType(type, pageReq)).thenReturn(emptyPage);

        // when
        Page<ProductDTO> result = productService.getProductsByType(type, pageReq);

        // then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

        verify(productsMicroserviceClient, times(1)).getProductByType(eq(type), eq(pageReq));
    }

    @Test
    void getProductsByType_SingleProductOfType_SingleProductPageReturned(){
        // given
        ProductDTO singleProduct = new ProductDTO("Gaming Laptop", BigDecimal.valueOf(4500.00), ProductsType.COMPUTER, 3, List.of());

        ProductsType type = ProductsType.COMPUTER;
        Pageable pageReq = PageRequest.of(0, 1);
        PageImpl<ProductDTO> singleProductPage = new PageImpl<>(List.of(singleProduct), pageReq, 1);

        when(productsMicroserviceClient.getProductByType(type, pageReq)).thenReturn(singleProductPage);

        // when
        Page<ProductDTO> result = productService.getProductsByType(type, pageReq);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Gaming Laptop", result.getContent().get(0).getName());
        assertEquals(ProductsType.COMPUTER, result.getContent().get(0).getType());
        assertEquals(BigDecimal.valueOf(4500.00), result.getContent().get(0).getPrice());
        assertEquals(3, result.getContent().get(0).getQuantity());

        verify(productsMicroserviceClient, times(1)).getProductByType(eq(type), eq(pageReq));
    }
}
