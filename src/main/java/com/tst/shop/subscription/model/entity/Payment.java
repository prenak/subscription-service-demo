package com.tst.shop.subscription.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 4900730320776553310L;

    @Id
    private Long paymentId;
    private BigDecimal amount;
    private String mode;
    private Integer status;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;
}
