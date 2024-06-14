package com.basel.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseRequest(
        @NotNull(message = "Product is required.")
        Integer productId,
        @NotNull(message = "Quantity is required.")
        @Positive(message = "Quantity must be positive.")
        Integer quantity
) {
}
