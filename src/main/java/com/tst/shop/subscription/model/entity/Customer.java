package com.tst.shop.subscription.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "subscriptions")
@ToString(exclude = {"subscriptions", "password"})
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = -4148899963008609820L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customerId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean active;
    private String role;
    //private Timestamp createdTimestamp;
    //private Timestamp updatedTimestamp;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("customer")
    private List<Subscription> subscriptions = new ArrayList<>();
}
