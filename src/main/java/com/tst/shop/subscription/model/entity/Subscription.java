package com.tst.shop.subscription.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@ToString(exclude = {"customer","product","payment"})
@EqualsAndHashCode(exclude = {"customer","product","payment"})
@Entity
@Table(name = "Subscription")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 8879426420196312959L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long subscriptionId;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Timestamp lastPausedTimestamp;
    private Boolean isActive = Boolean.TRUE;
    private Boolean isCancelled = Boolean.FALSE;
    private String voucherCode;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonIgnoreProperties("subscriptions")
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    private Product product;

    @OneToOne(fetch = FetchType.EAGER)
    private Payment payment;
}
