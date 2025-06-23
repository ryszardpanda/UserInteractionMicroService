package com.UserInteraction.UserInteractionMicroService.controller;

import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import com.UserInteraction.UserInteractionMicroService.service.facade.ProductFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-interaction/products")
@Slf4j
public class ProductController {
    private final ProductFacadeService productFacadeService;

    @GetMapping
    Page<ProductDTO> getProducts(@ParameterObject Pageable pagebale){
        return productFacadeService.getProducts(pagebale);
    }
}
