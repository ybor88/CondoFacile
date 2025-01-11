package com.condofacile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CondoFacileApplication {

    public static void main(String[] args) {
        SpringApplication.run(CondoFacileApplication.class, args);
        System.out.println("CondoFacile Backend is running!");
    }
}