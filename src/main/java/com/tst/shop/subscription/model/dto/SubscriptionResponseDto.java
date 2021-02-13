package com.tst.shop.subscription.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SubscriptionResponseDto {

    private Long subscriptionId;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Timestamp lastPausedTimestamp;
    private Boolean isActive;
    private Boolean isCancelled;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;
    private ProductInfoDto product;
    private PaymentInfoDto payment;
}
