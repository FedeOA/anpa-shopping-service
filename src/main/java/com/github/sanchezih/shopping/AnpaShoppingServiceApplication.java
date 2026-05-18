package com.github.sanchezih.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AnpaShoppingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnpaShoppingServiceApplication.class, args);
	}

}
