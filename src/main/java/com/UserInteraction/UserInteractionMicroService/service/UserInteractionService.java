package com.UserInteraction.UserInteractionMicroService.service;

import com.UserInteraction.UserInteractionMicroService.service.facade.CartFacadeService;
import com.UserInteraction.UserInteractionMicroService.service.facade.OrderFacadeService;
import com.UserInteraction.UserInteractionMicroService.service.facade.ProductFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInteractionService {
    private final CartFacadeService cart;
    private final ProductFacadeService product;
    private final OrderFacadeService order;
}
