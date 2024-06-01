package com.basel.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        Integer id,
        @NotNull(message = "Product name id required.")
        String name,
        @NotNull(message = "Product description id required.")
        String description,
        @Positive(message = "Available quantity should be positive.")
        Double availableQuantity,
        @Positive(message = "Price should be positive.")
        BigDecimal price,
        @NotNull(message = "Product category id required.")
        Integer categoryId
) {
}
