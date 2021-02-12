package com.tst.shop.subscription.model;

import com.tst.shop.subscription.model.entity.Customer;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomerUserDetails implements UserDetails {

    private Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(customer.getRole());
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(grantedAuthority);
        return roles;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return customer.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return customer.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return customer.isActive();
    }

    @Override
    public boolean isEnabled() {
        return customer.isActive();
    }
}
