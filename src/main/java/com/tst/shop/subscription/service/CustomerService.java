package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findCustomerById(Long customerId) throws Exception {
        return customerRepository.findById(customerId).orElseThrow(() -> new Exception("Customer not found"));
    }
}
