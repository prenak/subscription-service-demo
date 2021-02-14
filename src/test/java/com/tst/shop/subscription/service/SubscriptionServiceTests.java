package com.tst.shop.subscription.service;

import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.entity.Customer;
import com.tst.shop.subscription.model.entity.Subscription;
import com.tst.shop.subscription.repository.PaymentRepository;
import com.tst.shop.subscription.repository.SubscriptionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SubscriptionService.class})
public class SubscriptionServiceTests {

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ProductService productService;

    @MockBean
    private VoucherService voucherService;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @MockBean
    private PaymentRepository paymentRepository;

    @Captor
    private ArgumentCaptor<Subscription> subscriptionArgumentCaptor;


    @Before
    public void setUp() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setEmail("abc@gmail.com");

        Mockito.when(customerService.findCustomerByEmail(Mockito.anyString())).thenReturn(customer);

        Mockito.when(subscriptionRepository.save(Mockito.any(Subscription.class)))
                .thenAnswer(invocationOnMock -> {
                    return invocationOnMock.getArgument(0);
                });
    }


    @Test
    public void test_PauseSubscriptionForCustomer_ForIllegalArguments(){
        Throwable thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer(null, null);
        });
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Customer email cannot be null");

        thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer("abc@gmail.com", null);
        });
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Subscription id cannot be null");
    }


    @Test
    public void test_PauseSubscriptionForCustomer_WhenSubscriptionDoesNotBelongToCustomer(){
        Mockito.when(subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer("abc@gmail.com", 5L);
        });

        assertThat(thrown)
                .isInstanceOf(SubscriptionServiceException.class)
                .hasMessage("No such subscription available under this customer");
    }


    @Test
    public void test_PauseSubscriptionForCustomer_WhenSubscriptionIsAlreadyCancelled(){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId(4L);
        subscription.setIsCancelled(true);
        Mockito.when(subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(subscription));

        Throwable thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer("abc@gmail.com", 4L);
        });

        assertThat(thrown)
                .isInstanceOf(SubscriptionServiceException.class)
                .hasMessage("Can not action cancelled subscription");
    }


    @Test
    public void test_PauseSubscriptionForCustomer_WhenSubscriptionIsAlreadyPaused(){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId(4L);
        subscription.setIsActive(false);
        subscription.setIsCancelled(false);
        Mockito.when(subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(subscription));

        Throwable thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer("abc@gmail.com", 4L);
        });

        assertThat(thrown)
                .isInstanceOf(SubscriptionServiceException.class)
                .hasMessage("Cannot pause an already paused subscription");
    }

    @Test
    public void test_PauseSubscriptionForCustomer(){
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId(4L);
        subscription.setIsActive(true);
        subscription.setIsCancelled(false);
        Mockito.when(subscriptionRepository.findBySubscriptionIdAndCustomer_CustomerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(subscription));

        Throwable thrown = catchThrowable(() -> {
            subscriptionService.pauseSubscriptionForCustomer("abc@gmail.com", 4L);
        });

        Mockito.verify(subscriptionRepository, Mockito.atMostOnce()).save(subscriptionArgumentCaptor.capture());
        Subscription modifiedSubscription = subscriptionArgumentCaptor.getValue();
        assertThat(modifiedSubscription).isNotNull();
        assertThat(modifiedSubscription.getIsActive()).isFalse();
        assertThat(modifiedSubscription.getLastPausedTimestamp()).isNotNull();
    }
}
