package com.basel.ecommerce.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/createPayment")
    public ResponseEntity<Object> createPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequest), HttpStatus.CREATED);
    }

}
