package com.tst.shop.subscription.service;

import com.tst.shop.subscription.constant.SubscriptionServiceError;
import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer findCustomerByEmail(String email) throws Exception {
        Assert.hasText(email, "Email cannot be null");
        log.debug("Finding customer by email {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.CUSTOMER_NOT_FOUND));
        log.info("Fetched Customer: {} ", customer);
        return customer;
    }
}
