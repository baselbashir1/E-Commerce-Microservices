package com.basel.ecommerce.kafka;

import com.basel.ecommerce.customer.CustomerResponse;
import com.basel.ecommerce.order.PaymentMethod;
import com.basel.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
