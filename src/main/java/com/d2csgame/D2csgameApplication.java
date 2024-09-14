package com.d2csgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class D2csgameApplication {

    public static void main(String[] args) {
        SpringApplication.run(D2csgameApplication.class, args);
    }

}
