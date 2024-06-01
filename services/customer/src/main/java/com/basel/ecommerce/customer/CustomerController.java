package com.basel.ecommerce.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/createCustomer")
    public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        customerService.createCustomer(customerRequest);
        return new ResponseEntity<>("Customer created successfully.", HttpStatus.CREATED);
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<Object> updateCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        customerService.updateCustomer(customerRequest);
        return new ResponseEntity<>("Customer updated successfully.", HttpStatus.ACCEPTED);
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<Object> getAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/exists/{customerId}")
    public ResponseEntity<Object> existsById(@PathVariable("customerId") String customerId) {
        return new ResponseEntity<>(customerService.existsById(customerId), HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("customerId") String customerId) {
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Object> deleteCustomerById(@PathVariable("customerId") String customerId) {
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity<>("Customer deleted successfully.", HttpStatus.ACCEPTED);
    }

}
