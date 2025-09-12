package com.UserInteraction.UserInteractionMicroService.client.exceptions.handler;

import com.UserInteraction.UserInteractionMicroService.client.exceptions.CartNotFoundException;
import com.UserInteraction.UserInteractionMicroService.client.exceptions.ErrorMessage;
import com.UserInteraction.UserInteractionMicroService.client.exceptions.OrderNotFoundException;
import com.UserInteraction.UserInteractionMicroService.client.exceptions.ProductNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserInteractionExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleOrderNotFoundException(
            OrderNotFoundException ex) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(ex.getMessage()), new HttpHeaders(), ex.getHttpStatus());
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleProductNotFoundException(
            ProductNotFoundException ex) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(ex.getMessage()), new HttpHeaders(), ex.getHttpStatus());
    }

    @ExceptionHandler({CartNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleCartNotFoundException(
            CartNotFoundException ex) {
        return new ResponseEntity<ErrorMessage>(
                new ErrorMessage(ex.getMessage()), new HttpHeaders(), ex.getHttpStatus());
    }
}
