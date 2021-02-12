package com.tst.shop.subscription.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    private Integer voucherId;
    private String code;
    private String description;
    private Short percentageDiscount;
    private Timestamp expiryTimestamp;

    @ManyToMany
    @JsonIgnoreProperties("vouchers")
    private List<Product> products;
}
