package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.model.entity.Voucher;
import com.tst.shop.subscription.repository.ProductRepository;
import com.tst.shop.subscription.repository.VoucherRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VoucherService voucherService;


    public List<Product> findAllProducts(String voucherCode) throws Exception {

        if (!StringUtils.hasText(voucherCode)) return productRepository.findAll();

        Voucher voucher = voucherService.fetchVoucherDetailsByCode(voucherCode).orElseThrow(() -> new Exception("No products available for this voucher code"));
        return voucher.getProducts();
    }

    public Product findProductById(Integer productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
    }
}
