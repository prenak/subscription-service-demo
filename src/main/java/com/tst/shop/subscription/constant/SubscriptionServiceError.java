package com.tst.shop.subscription.constant;

import lombok.Getter;

@Getter
public enum SubscriptionServiceError {

    INVALID_PRODUCT_ID ("No product found for the given product id"),
    INVALID_VOUCHER_CODE ("Voucher is not valid"),
    NO_PRODUCT_FOR_VC ("No products available for this voucher code"),
    NO_VOUCHER_FOR_PRODUCT ("Voucher is not applicable for this product"),
    PRODUCT_ALREADY_SUBSCRIBED ("Already subscribed for this product"),
    NO_SUBSCRIPTION_UNDER_CUSTOMER ("No such subscription available under this customer"),
    CANNOT_ACTION_CANCELLED_SUBS ("Can not action cancelled subscription"),
    SUBSCRIPTION_ALREADY_PAUSED ("Cannot pause an already paused subscription"),
    SUBSCRIPTION_ALREADY_ONGOING ("Cannot resume an ongoing subscription"),
    CUSTOMER_NOT_FOUND ("Customer not found")

    ;

    private String message;

    SubscriptionServiceError(String message) {
        this.message = message;
    }
}
