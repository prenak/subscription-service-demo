package com.tst.shop.subscription.api;


import com.tst.shop.subscription.model.dto.SubscriptionRequestDto;
import com.tst.shop.subscription.model.dto.SubscriptionResponseDto;
import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.service.CustomerService;
import com.tst.shop.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public SubscriptionResponseDto subscribeToProduct(@RequestBody SubscriptionRequestDto subscriptionRequestDto, Principal principal) {
        log.info("Received a request to create a new subscription: {} for customer {}", subscriptionRequestDto, principal.getName());
        SubscriptionResponseDto subscriptionResponseDto = null;
        try {
            Subscription subscription = subscriptionService.createNewSubscription(principal.getName(), subscriptionRequestDto.getProductId(),
                    subscriptionRequestDto.getStartTimestamp(), subscriptionRequestDto.getVoucherCode());
            subscriptionResponseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return subscriptionResponseDto;
    }


    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public List<SubscriptionResponseDto> getSubscriptions(Principal principal){
        List<SubscriptionResponseDto> subscriptionResponseDtos = new ArrayList<>();
        try{
            List<Subscription> subscriptions = subscriptionService.fetchAllSubscriptionsForCustomer(principal.getName());
            subscriptions.forEach(subscription -> {
                subscriptionResponseDtos.add(modelMapper.map(subscription, SubscriptionResponseDto.class));
            });

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return subscriptionResponseDtos;
    }

    @PatchMapping("/{subscriptionId}/pause")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public SubscriptionResponseDto pauseSubscription(@PathVariable("subscriptionId") Long subscriptionId, Principal principal) {
        log.info("Received a request to pause subscription: {} for customer {}", subscriptionId, principal.getName());
        SubscriptionResponseDto subscriptionResponseDto = null;
        try {
            Subscription subscription = subscriptionService.pauseSubscriptionForCustomer(principal.getName(), subscriptionId);
            subscriptionResponseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return subscriptionResponseDto;
    }


    @PatchMapping("/{subscriptionId}/resume")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public SubscriptionResponseDto resumeSubscription(@PathVariable("subscriptionId") Long subscriptionId, Principal principal) {
        log.info("Received a request to resume subscription: {} for customer {}", subscriptionId, principal.getName());
        SubscriptionResponseDto subscriptionResponseDto = null;
        try {
            Subscription subscription = subscriptionService.unPauseSubscriptionForCustomer(principal.getName(), subscriptionId);
            subscriptionResponseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return subscriptionResponseDto;
    }


    @DeleteMapping("/{subscriptionId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public boolean cancelSubscription(@PathVariable("subscriptionId") Long subscriptionId, Principal principal) {
        log.info("Received a request to cancel subscription: {} for customer {}", subscriptionId, principal.getName());
        try {
            subscriptionService.cancelSubscriptionForCustomer(principal.getName(), subscriptionId);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return true;
    }
}
