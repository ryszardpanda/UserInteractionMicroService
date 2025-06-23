package com.UserInteraction.UserInteractionMicroService.service.facade;

import com.UserInteraction.UserInteractionMicroService.client.product.ProductsMicroserviceClient;
import com.UserInteraction.UserInteractionMicroService.dto.product.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFacadeService {
    private final ProductsMicroserviceClient productsMicroserviceClient;

   public Page<ProductDTO> getProducts(Pageable pagebale){
       return productsMicroserviceClient.getProducts(pagebale);
    }
}
