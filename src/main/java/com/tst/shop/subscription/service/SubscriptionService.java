package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.model.entity.Payment;
import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.repository.PaymentRepository;
import com.tst.shop.subscription.repository.SubscriptionRepository;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
    public Subscription createNewSubscription(String customerEmail, Integer productId, Date startDate) throws Exception {
        Subscription subscription = new Subscription();
        subscription.setCustomer(customerService.findCustomerByEmail(customerEmail));

        Product product = productService.findProductById(productId);
        subscription.setProduct(product);

        DateTime startDt = new DateTime(startDate);
        subscription.setStartTimestamp(new Timestamp(startDt.getMillis()));
        DateTime endDt = startDt.plusWeeks(product.getDurationInWeeks());
        subscription.setEndTimestamp(new Timestamp(endDt.getMillis()));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        subscription.setCreatedTimestamp(now);
        subscription.setUpdatedTimestamp(now);

        Payment payment = new Payment();
        payment.setAmount(product.getPrice());
        payment.setMode("CC");
        payment.setStatus(1);
        subscription.setPayment(paymentRepository.save(payment));

        return subscriptionRepository.save(subscription);
    }


    public List<Subscription> fetchAllSubscriptionsForCustomer(String customerEmail) throws Exception {
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        return subscriptionRepository.findAllByCustomer_CustomerId(customer.getCustomerId());
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription pauseSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new Exception("No such subscription available under this customer"));

        if (subscription.getIsCancelled()) throw new Exception("Cannot resume a cancelled subscription");
        if (!subscription.getIsActive()) throw new Exception("Cannot pause an already paused subscription");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        subscription.setUpdatedTimestamp(now);
        subscription.setLastPausedTimestamp(now);
        subscription.setIsActive(false);

        return subscriptionRepository.save(subscription);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription unPauseSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new Exception("No such subscription available under this customer"));

        if (subscription.getIsCancelled()) throw new Exception("Cannot resume a cancelled subscription");
        if (subscription.getIsActive()) throw new Exception("Cannot resume an ongoing subscription");

        DateTime lastPausedTime = new DateTime(subscription.getLastPausedTimestamp());
        DateTime now = new DateTime();
        int differenceInDays = Days.daysBetween(lastPausedTime, now).getDays();

        DateTime subscriptionEndDt = new DateTime(subscription.getEndTimestamp());
        subscriptionEndDt = subscriptionEndDt.plusDays(differenceInDays);
        subscription.setEndTimestamp(new Timestamp(subscriptionEndDt.getMillis()));

        subscription.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
        subscription.setIsActive(true);

        return subscriptionRepository.save(subscription);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription cancelSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new Exception("No such subscription available under this customer"));

        if (subscription.getIsCancelled()) throw new Exception("Cannot resume a cancelled subscription");

        subscription.setIsCancelled(true);
        subscription.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));

        return subscriptionRepository.save(subscription);
    }
}
