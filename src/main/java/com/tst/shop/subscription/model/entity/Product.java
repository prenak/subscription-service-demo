package com.tst.shop.subscription.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = -2488936673413390129L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;
    private String name;
    private String description;
    private String trainingLevel;
    private BigDecimal price;
    private Boolean status = Boolean.TRUE;
    private Integer durationInWeeks;
    //private Timestamp createdTimestamp;
    //private Timestamp updatedTimestamp;
}
