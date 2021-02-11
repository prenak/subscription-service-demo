package com.tst.shop.subscription.service;

import com.tst.shop.subscription.model.entity.Product;
import com.tst.shop.subscription.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public Product findProductById(Integer productId) throws Exception {
        return productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
    }
}
