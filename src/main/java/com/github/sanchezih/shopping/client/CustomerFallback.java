package com.github.sanchezih.shopping.client;

import com.github.sanchezih.shopping.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerFallback {

    public Customer fallback(Throwable throwable) {
        return Customer.builder().firstName("none").lastName("none").email("none").photoUrl("none").build();
    }
}
