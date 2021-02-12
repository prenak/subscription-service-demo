package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findCustomerById(Long customerId) throws Exception {
        return customerRepository.findById(customerId).orElseThrow(() -> new Exception("Customer not found"));
    }

    public Customer findCustomerByEmail(String email) throws Exception {
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new Exception("Customer not found"));
        log.info("Customer by email: " + customer);
        return customer;
    }
}
