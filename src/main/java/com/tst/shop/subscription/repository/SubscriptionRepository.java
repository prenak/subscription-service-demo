package com.tst.shop.subscription.repository;

import com.tst.shop.subscription.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByCustomer_CustomerId(Long customerId);
}
