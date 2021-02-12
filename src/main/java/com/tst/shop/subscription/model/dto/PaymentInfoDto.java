package com.tst.shop.subscription.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentInfoDto {

    private Long paymentId;
    private BigDecimal amount;
    private String mode;
    private Integer status;
}
