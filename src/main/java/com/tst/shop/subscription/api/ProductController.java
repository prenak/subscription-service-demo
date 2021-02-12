package com.tst.shop.subscription.api;


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

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Product> fetchAllProducts(@PathParam("voucherCode") String voucherCode) {
        try{
            return productService.findAllProducts(voucherCode);

        } catch (Exception ex){
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }

    @GetMapping("/{productId}")
    public Product fetchOneProduct(@PathVariable("productId") Integer productId){
        try {
            return productService.findProductById(productId);

        } catch (Exception ex) {
            log.error("Exception - {}", ex.getMessage(), ex);
            ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            log.info("Returning ResponseStatusException: ", responseStatusException);
            throw responseStatusException;
        }
    }
}
