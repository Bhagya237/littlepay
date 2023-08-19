package com.littlepay;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

/**
 * Hello littlePay!
 */
public class LittlePayApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(LittlePayApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Hello littlePay!");
    }
}
