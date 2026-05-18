package com.github.sanchezih.shopping.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.github.sanchezih.shopping.model.Customer;

@FeignClient(name = "anpa-customer-service", path = "/api/v1/customers")
public interface CustomerClient {

	@GetMapping(value = "/{id}")
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id);

}