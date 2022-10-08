package com.example.shoppingmall_restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShoppingMallRestapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMallRestapiApplication.class, args);
    }

}
