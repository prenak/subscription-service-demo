package com.tst.shop.subscription.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 4900730320776553310L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private BigDecimal amount;
    private String mode;
    private Integer status = 1;
    //private Timestamp createdTimestamp;
    //private Timestamp updatedTimestamp;
}
