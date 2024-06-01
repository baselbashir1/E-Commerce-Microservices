package com.basel.ecommerce.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        return productRequest == null ? null : Product.builder()
                .id(productRequest.id())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .availableQuantity(productRequest.availableQuantity())
                .category(Category.builder()
                        .id(productRequest.categoryId())
                        .build())
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return product == null ? null :
                new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getAvailableQuantity(),
                        product.getPrice(),
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getDescription()
                );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(Product product, double quantity) {
        return product == null ? null :
                new ProductPurchaseResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        quantity
                );
    }

}
