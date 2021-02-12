package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Voucher;
import com.tst.shop.subscription.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;


    public Optional<Voucher> fetchVoucherDetailsByCode(String voucherCode) {
        return voucherRepository.findByCode(voucherCode);
    }

    public boolean isValidVoucher(String voucherCode){
        return voucherRepository.findByCode(voucherCode).isPresent();
    }
}
