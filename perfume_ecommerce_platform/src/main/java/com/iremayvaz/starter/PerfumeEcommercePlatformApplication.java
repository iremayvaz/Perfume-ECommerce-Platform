package com.iremayvaz.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan(basePackages = {"com.iremayvaz"})
@SpringBootApplication
public class PerfumeEcommercePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfumeEcommercePlatformApplication.class, args);
    }

}
