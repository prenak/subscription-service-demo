package com.tst.shop.subscription.exception;

import com.tst.shop.subscription.constant.SubscriptionServiceError;

public class SubscriptionServiceException extends Exception {

    public SubscriptionServiceException(SubscriptionServiceError subscriptionServiceError) {
        super(subscriptionServiceError.getMessage());
    }
}
