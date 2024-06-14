package com.basel.ecommerce.order;

import com.basel.ecommerce.customer.CustomerClient;
import com.basel.ecommerce.customer.CustomerResponse;
import com.basel.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;

    public Integer createOrder(OrderRequest orderRequest) {
        CustomerResponse customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: no customer exists with the provided ID."));


        return null;
    }

}
