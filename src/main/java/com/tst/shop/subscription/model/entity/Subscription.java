package com.tst.shop.subscription.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "Subscription")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 8879426420196312959L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subscriptionId;
    private Timestamp startTimestamp;
    private Timestamp endTimestamp;
    private Timestamp lastPausedTimestamp;
    private Boolean isActive = Boolean.TRUE;
    private Timestamp createdTimestamp;
    private Timestamp updatedTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;
}
