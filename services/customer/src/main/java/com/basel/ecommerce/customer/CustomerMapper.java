package com.basel.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {
        return customerRequest == null ? null : Customer.builder()
                .id(customerRequest.id())
                .firstname(customerRequest.firstname())
                .lastname(customerRequest.lastname())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return customer == null ? null :
                new CustomerResponse(
                        customer.getId(),
                        customer.getFirstname(),
                        customer.getLastname(),
                        customer.getEmail(),
                        customer.getAddress()
                );
    }

}
