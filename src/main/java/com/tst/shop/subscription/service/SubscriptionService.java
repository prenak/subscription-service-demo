package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Payment;
import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.repository.PaymentRepository;
import com.tst.shop.subscription.repository.SubscriptionRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class SubscriptionService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createNewSubscription(Long customerId, Integer productId) throws Exception {
        Subscription subscription = new Subscription();
        subscription.setCustomer(customerService.findCustomerById(customerId));

        Product product = productService.findProductById(productId);
        subscription.setProduct(product);

        DateTime startDt = new DateTime();
        Timestamp startTs = new Timestamp(startDt.getMillis());
        subscription.setStartTimestamp(startTs);
        subscription.setCreatedTimestamp(startTs);
        subscription.setUpdatedTimestamp(startTs);
        DateTime endDt = startDt.plusWeeks(product.getDurationInWeeks());
        subscription.setEndTimestamp(new Timestamp(endDt.getMillis()));

        Payment payment = new Payment();
        payment.setAmount(product.getPrice());
        payment.setMode("CC");
        payment.setStatus(1);
        subscription.setPayment(paymentRepository.save(payment));

        subscription = subscriptionRepository.save(subscription);
        return subscription.getSubscriptionId();
    }
}
