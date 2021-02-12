package com.tst.shop.subscription.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoDto {

    private Integer productId;
    private String name;
    private String description;
    private String trainingLevel;
    private BigDecimal price;
    private Integer durationInWeeks;
    private Integer accessType;
}
