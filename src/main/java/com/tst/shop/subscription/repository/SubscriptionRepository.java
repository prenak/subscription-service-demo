package com.tst.shop.subscription.repository;

import com.tst.shop.subscription.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByCustomer_CustomerId(Long customerId);

    Optional<Subscription> findBySubscriptionIdAndCustomer_CustomerId(Long subscriptionId, Long customerId);

    Optional<Subscription> findByCustomer_CustomerIdAndProduct_ProductId(Long customerId, Integer productId);
}
