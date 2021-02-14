package com.tst.shop.subscription.api;


import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.dto.SubscriptionRequestDto;
import com.tst.shop.subscription.model.dto.SubscriptionResponseDto;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * SubscriptionController
 * - API to deal with all the subscription related functionalities
 */

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubscriptionService subscriptionService;



    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public SubscriptionResponseDto subscribeToProduct(@RequestBody SubscriptionRequestDto subscriptionRequestDto, Principal principal) {
        log.info("Received a request to create a new subscription: {} for customer {}", subscriptionRequestDto, principal.getName());
        SubscriptionResponseDto subscriptionResponseDto = null;
        try {
            Subscription subscription = subscriptionService.createNewSubscription(principal.getName(), subscriptionRequestDto.getProductId(),
                    subscriptionRequestDto.getStartTimestamp(), subscriptionRequestDto.getVoucherCode());
            subscriptionResponseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);

        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException - {}", iae.getMessage(), iae);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        log.debug("Returning {}", subscriptionResponseDto);
        return subscriptionResponseDto;
    }


    @GetMapping("")
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public List<SubscriptionResponseDto> getSubscriptions(Principal principal){
        log.info("Received a request to get subscriptions for customer {}", principal.getName());
        List<SubscriptionResponseDto> subscriptionResponseDtos = new ArrayList<>();
        try{
            List<Subscription> subscriptions = subscriptionService.fetchAllSubscriptionsForCustomer(principal.getName());
            subscriptions.forEach(subscription -> {
                subscriptionResponseDtos.add(modelMapper.map(subscription, SubscriptionResponseDto.class));
            });

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        log.debug("Returning subscriptions: {}", subscriptionResponseDtos);
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

        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException - {}", iae.getMessage(), iae);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        log.debug("Returning {}", subscriptionResponseDto);
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

        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException - {}", iae.getMessage(), iae);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        log.debug("Returning {}", subscriptionResponseDto);
        return subscriptionResponseDto;
    }


    @DeleteMapping("/{subscriptionId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','CUSTOMER_ON_TRAIL')")
    public boolean cancelSubscription(@PathVariable("subscriptionId") Long subscriptionId, Principal principal) {
        log.info("Received a request to cancel subscription: {} for customer {}", subscriptionId, principal.getName());
        try {
            subscriptionService.cancelSubscriptionForCustomer(principal.getName(), subscriptionId);

        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException - {}", iae.getMessage(), iae);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
        return true;
    }
}
