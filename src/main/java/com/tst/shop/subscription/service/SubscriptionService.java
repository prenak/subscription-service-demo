package com.tst.shop.subscription.service;

import com.tst.shop.subscription.constant.SubscriptionServiceError;
import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.entity.*;
import com.tst.shop.subscription.repository.PaymentRepository;
import com.tst.shop.subscription.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SubscriptionService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    /**
     * Creates a new subscription for customer for a product
     * @param customerEmail customer email required to create the subscription
     * @param productId product for which the subscription has to be made
     * @param startDate date to start the subscription from, if not provided, the current date will be considered
     * @param voucherCode voucher code to be applied while creating the subscription
     * @return instance of a new subscription created
     * @throws Exception any while creating a new subscription
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription createNewSubscription(String customerEmail, Integer productId, Date startDate, String voucherCode) throws Exception {
        log.debug("Creating a new subscription for customer: {}, productId: {} with startDate: {} and voucherCode: {}", customerEmail, productId, startDate, voucherCode);
        Assert.hasText(customerEmail, "Customer email cannot be null");
        Assert.notNull(productId, "Product id cannot be null");

        Subscription subscription = new Subscription();
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        log.debug("Customer details: {}", customer);

        if (checkIfSubscriptionAlreadyExists(customer.getCustomerId(), productId)) {
            throw new SubscriptionServiceException(SubscriptionServiceError.PRODUCT_ALREADY_SUBSCRIBED);
        }
        subscription.setCustomer(customer);

        Product product = productService.findProductById(productId);
        subscription.setProduct(product);
        log.debug("Product info: {}", product);

        // Set the start date to startDate provided or today
        DateTime startDt = new DateTime(ObjectUtils.isEmpty(startDate) ? new Date() : startDate);
        subscription.setStartTimestamp(new Timestamp(startDt.getMillis()));
        log.debug("Subscription start date set as: {}", subscription.getStartTimestamp());

        // Calculate the end date by adding product duration to start date
        DateTime endDt = startDt.plusWeeks(product.getDurationInWeeks());
        subscription.setEndTimestamp(new Timestamp(endDt.getMillis()));
        log.debug("Subscription end date set as: {}", subscription.getEndTimestamp());

        Short percentageDiscount = 0;
        if (StringUtils.hasText(voucherCode)) {
            log.debug("Checking if voucher code {} is valid and applicable", voucherCode);
            if (!voucherService.isValidVoucher(voucherCode)){
                throw new SubscriptionServiceException(SubscriptionServiceError.INVALID_VOUCHER_CODE);
            }

            // Check if voucher is applicable for this product
            Voucher voucher = product.getVouchers()
                    .stream()
                    .filter(v -> voucherCode.equalsIgnoreCase(v.getCode()))
                    .findFirst().orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.NO_VOUCHER_FOR_PRODUCT));

            log.info("Applying voucher: {}", voucher);
            percentageDiscount = voucher.getPercentageDiscount();
        }

        Payment payment = new Payment();
        BigDecimal originalPrice = product.getPrice();
        log.debug("Original product price: {}", originalPrice.toPlainString());
        BigDecimal amountToSubtract = originalPrice.multiply(new BigDecimal(percentageDiscount)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        log.debug("Amount to subtract after applying voucher: {}", amountToSubtract.toPlainString());
        payment.setAmount(originalPrice.subtract(amountToSubtract));
        payment.setMode("Credit Card");
        payment = paymentRepository.save(payment);

        log.info("Created payment: {}", payment);
        subscription.setPayment(payment);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        subscription.setCreatedTimestamp(now);
        subscription.setUpdatedTimestamp(now);

        subscription = subscriptionRepository.save(subscription);
        log.info("Successfully subscribed with subscription id: {}", subscription.getSubscriptionId());
        return subscription;
    }


    /**
     * Retrieves all the subscriptions for the customer
     * @param customerEmail customer email
     * @return list of subscriptions made by the customer
     * @throws Exception any while fetching the customer
     */
    public List<Subscription> fetchAllSubscriptionsForCustomer(String customerEmail) throws Exception {
        log.debug("Fetching all subscriptions for customer: {}", customerEmail);
        Assert.hasText(customerEmail, "Customer email cannot be null");
        Customer customer = customerService.findCustomerByEmail(customerEmail);

        List<Subscription> subscriptions = subscriptionRepository.findAllByCustomer_CustomerId(customer.getCustomerId());
        log.info("Found {} subscriptions made by customer {}", subscriptions.size(), customerEmail);
        return subscriptions;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription pauseSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        log.debug("Pausing subscription {} for customer: {}", subscriptionId, customerEmail);
        Assert.hasText(customerEmail, "Customer email cannot be null");
        Assert.notNull(subscriptionId, "Subscription id cannot be null");

        Customer customer = customerService.findCustomerByEmail(customerEmail);

        log.debug("Fetching subscription {} for customerId {}", subscriptionId, customer.getCustomerId());
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.NO_SUBSCRIPTION_UNDER_CUSTOMER));

        if (subscription.getIsCancelled()) throw new SubscriptionServiceException(SubscriptionServiceError.CANNOT_ACTION_CANCELLED_SUBS);
        if (!subscription.getIsActive()) throw new SubscriptionServiceException(SubscriptionServiceError.SUBSCRIPTION_ALREADY_PAUSED);

        log.debug("Pausing subscription: {}", subscription);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        subscription.setUpdatedTimestamp(now);
        subscription.setLastPausedTimestamp(now);
        subscription.setIsActive(false);

        subscription = subscriptionRepository.save(subscription);
        log.info("Successfully paused the subscription");
        return subscription;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription unPauseSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        log.debug("Resuming subscription {} for customer: {}", subscriptionId, customerEmail);
        Assert.hasText(customerEmail, "Customer email cannot be null");
        Assert.notNull(subscriptionId, "Subscription id cannot be null");

        Customer customer = customerService.findCustomerByEmail(customerEmail);

        log.debug("Fetching subscription {} for customerId {}", subscriptionId, customer.getCustomerId());
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.NO_SUBSCRIPTION_UNDER_CUSTOMER));

        if (subscription.getIsCancelled()) throw new SubscriptionServiceException(SubscriptionServiceError.CANNOT_ACTION_CANCELLED_SUBS);
        if (subscription.getIsActive()) throw new SubscriptionServiceException(SubscriptionServiceError.SUBSCRIPTION_ALREADY_ONGOING);

        log.debug("Resuming subscription: {}", subscription);
        DateTime lastPausedTime = new DateTime(subscription.getLastPausedTimestamp());
        DateTime now = new DateTime();
        int differenceInDays = Days.daysBetween(lastPausedTime, now).getDays();

        log.info("Extending the end date of subscription {} by {} days", subscription.getSubscriptionId(), differenceInDays);
        DateTime subscriptionEndDt = new DateTime(subscription.getEndTimestamp());
        subscriptionEndDt = subscriptionEndDt.plusDays(differenceInDays);
        subscription.setEndTimestamp(new Timestamp(subscriptionEndDt.getMillis()));

        subscription.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));
        subscription.setIsActive(true);

        subscription = subscriptionRepository.save(subscription);
        log.info("Successfully resumed the subscription");
        return subscription;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Subscription cancelSubscriptionForCustomer(String customerEmail, Long subscriptionId) throws Exception {
        log.debug("Cancelling subscription {} for customer: {}", subscriptionId, customerEmail);
        Assert.hasText(customerEmail, "Customer email cannot be null");
        Assert.notNull(subscriptionId, "Subscription id cannot be null");

        Customer customer = customerService.findCustomerByEmail(customerEmail);

        log.debug("Fetching subscription {} for customerId {}", subscriptionId, customer.getCustomerId());
        Subscription subscription = subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(subscriptionId, customer.getCustomerId())
                .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.NO_SUBSCRIPTION_UNDER_CUSTOMER));

        if (subscription.getIsCancelled()) throw new SubscriptionServiceException(SubscriptionServiceError.CANNOT_ACTION_CANCELLED_SUBS);

        subscription.setIsCancelled(true);
        subscription.setUpdatedTimestamp(new Timestamp(System.currentTimeMillis()));

        subscription = subscriptionRepository.save(subscription);
        log.info("Successfully cancelled the subscription");
        return subscription;
    }


    private boolean checkIfSubscriptionAlreadyExists(Long customerId, Integer productId) {
        log.debug("Checking if subscription already exists for customerId {} on productId {}", customerId, productId);
        boolean flag = subscriptionRepository.findByCustomer_CustomerIdAndProduct_ProductId(customerId, productId).isPresent();
        log.info("Does subscription exists for customerId {} and productId {}?: {}", customerId, productId, flag);
        return flag;
    }
}
