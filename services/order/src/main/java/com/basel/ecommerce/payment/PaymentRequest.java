package com.basel.ecommerce.payment;

import com.basel.ecommerce.customer.CustomerResponse;
import com.basel.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
