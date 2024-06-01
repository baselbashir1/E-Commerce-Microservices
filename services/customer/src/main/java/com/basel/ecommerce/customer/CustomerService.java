package com.basel.ecommerce.customer;

import com.basel.ecommerce.exceptions.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest customerRequest) {
        Customer customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() ->
                        new CustomerNotFoundException(String.format("Cannot update customer:: No customer found with the provided Id:: %s", customerRequest.id())));
        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId).isPresent();
    }

    public CustomerResponse getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::toCustomerResponse)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with provided Id:: $s", customerId)));
    }

    public void deleteCustomerById(String customerId) {
        customerRepository.deleteById(customerId);
    }

    public void mergeCustomer(Customer customer, CustomerRequest customerRequest) {
        if (StringUtils.isNotBlank(customerRequest.firstname())) {
            customer.setFirstname(customerRequest.firstname());
        }
        if (StringUtils.isNotBlank(customerRequest.lastname())) {
            customer.setLastname(customerRequest.lastname());
        }
        if (StringUtils.isNotBlank(customerRequest.email())) {
            customer.setEmail(customerRequest.email());
        }
        if (customerRequest.address() != null) {
            customer.setAddress(customerRequest.address());
        }
    }

}
