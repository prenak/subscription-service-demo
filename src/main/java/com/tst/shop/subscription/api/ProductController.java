package com.tst.shop.subscription.api;


import com.tst.shop.subscription.exception.SubscriptionServiceException;
import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

/**
 * ProductController
 * - API for accessing product related details
 */


@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Product> fetchAllProducts(@PathParam("voucherCode") String voucherCode) {
        log.info("Received a request to fetch all products. Voucher code passed: {}", voucherCode);
        try{
            return productService.findAllProducts(voucherCode);

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.NOT_FOUND, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex){
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }


    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.FOUND)
    public Product fetchOneProduct(@PathVariable("productId") Integer productId){
        log.info("Received a request to fetch a product by productId: {}", productId);
        try {
            return productService.findProductById(productId);

        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException - {}", iae.getMessage(), iae);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (SubscriptionServiceException sse) {
            log.error("SubscriptionServiceException - {}", sse.getMessage(), sse);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.NOT_FOUND, sse.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }
}
