package com.tst.shop.subscription.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionRequestDto {

    private String requestId;
    private Integer productId;
    private Date startTimestamp;
}
