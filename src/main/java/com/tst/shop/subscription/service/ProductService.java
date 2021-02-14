package com.tst.shop.subscription.service;

import com.tst.shop.subscription.constant.SubscriptionServiceError;
import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.model.entity.Voucher;
import com.tst.shop.subscription.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VoucherService voucherService;


    /**
     * Fetch all products. Filters in the products applicable for the given voucher code if its valid.
     * @param voucherCode : a valid voucher code
     * @return list of products [applicable for the given voucher code]
     * @throws Exception if the voucher code is invalid
     */
    public List<Product> findAllProducts(String voucherCode) throws Exception {
        log.debug("Fetching all products. Filtering by voucher code {} if applicable", voucherCode);
        List<Product> products = null;

        if (StringUtils.hasText(voucherCode)) {
            log.debug("Fetching voucher by code {}", voucherCode);
            Voucher voucher = voucherService.fetchVoucherDetailsByCode(voucherCode)
                    .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.NO_PRODUCT_FOR_VC));
            products = voucher.getProducts();
            log.debug("Found {} products applicable for voucher code {}", products.size(), voucherCode);

        } else {
            products = productRepository.findAll();

        }
        log.info("Returning {} products", products.size());
        return products;
    }


    /**
     * Fetch one product by the given product id
     * @param productId : unique id of the product to fetch
     * @return product with the given product id
     * @throws Exception if the product id passed is invalid
     */
    public Product findProductById(Integer productId) throws Exception {
        log.debug("Fetching a product for productId: {}", productId);
        Assert.notNull(productId, "Product id passed is null");
        Product product =  productRepository.findById(productId)
                .orElseThrow(() -> new SubscriptionServiceException(SubscriptionServiceError.INVALID_PRODUCT_ID));
        log.info("Returning product: {}", product);
        return product;
    }
}
