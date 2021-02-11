package com.tst.shop.subscription.api;


import com.tst.shop.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/subscribe")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("")
    public Long subscribeToProduct(){
        try{
            return subscriptionService.createNewSubscription(1L, 2);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }
}
