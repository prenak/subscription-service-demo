package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.CustomerUserDetails;
import com.tst.shop.subscription.model.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDetailsSecurityService implements UserDetailsService {

    @Autowired
    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomerUserDetails customerUserDetails = new CustomerUserDetails();
        try{
            Customer customer = customerService.findCustomerByEmail(email);
            customerUserDetails.setCustomer(customer);

        } catch (Exception e) {
            throw new UsernameNotFoundException(String.format("%s not a valid customer", email));
        }
        return customerUserDetails;
    }
}
