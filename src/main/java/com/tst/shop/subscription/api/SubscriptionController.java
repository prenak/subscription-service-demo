package com.tst.shop.subscription.api;


import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.service.CustomerService;
import com.tst.shop.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public Long subscribeToProduct(Principal principal){
        try{
            return subscriptionService.createNewSubscription(1L, 2);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public List<Subscription> getSubscriptions(Authentication principal){
        try{
            Customer c = customerService.findCustomerById(1L);
            return c.getSubscriptions();

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }
}
