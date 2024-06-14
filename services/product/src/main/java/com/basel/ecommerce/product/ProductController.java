package com.basel.ecommerce.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<Object> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PostMapping("/purchaseProducts")
    public ResponseEntity<Object> purchaseProducts(@RequestBody @Valid List<ProductPurchaseRequest> purchaseRequestList) {
        return new ResponseEntity<>(productService.purchaseProducts(purchaseRequestList), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getProductById(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<Object> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

}
