package com.github.sanchezih.shopping.client;

import com.github.sanchezih.shopping.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFallback {

    public Product fallback(Throwable throwable) {
        return Product.builder().name("none").build();
    }
}
