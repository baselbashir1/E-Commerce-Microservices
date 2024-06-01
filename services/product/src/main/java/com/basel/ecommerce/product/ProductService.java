package com.basel.ecommerce.product;

import com.basel.ecommerce.exceptions.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest productRequest) {
        Product product = productRepository.save(productMapper.toProduct(productRequest));
        return product.getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        List<Integer> productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        List<Product> existingProducts = productRepository.findAllByIdInOrderById(productIds);
        if (productIds.size() != existingProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exists.");
        }

        List<ProductPurchaseRequest> requestedProducts = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        List<ProductPurchaseResponse> purchasedProducts = new ArrayList<>();
        for (int i = 0; i < existingProducts.size(); i++) {
            Product product = existingProducts.get(i);
            ProductPurchaseRequest productPurchaseRequest = requestedProducts.get(i);
            if (product.getAvailableQuantity() < productPurchaseRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with Id:: " + productPurchaseRequest.productId());
            }
            double newAvailableProducts = product.getAvailableQuantity() - productPurchaseRequest.quantity();
            product.setAvailableQuantity(newAvailableProducts);
            productRepository.save(product);
            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, productPurchaseRequest.quantity()));
        }
        return purchasedProducts;
    }

    public ProductResponse getProductById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with Id:: " + productId));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

}
