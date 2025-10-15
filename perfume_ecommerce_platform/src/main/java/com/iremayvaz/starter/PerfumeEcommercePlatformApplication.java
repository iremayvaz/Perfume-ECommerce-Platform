package com.iremayvaz.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan(basePackages = {"com.iremayvaz"})       // Database tablolarını görmek için
@ComponentScan(basePackages = {"com.iremayvaz"})
@SpringBootApplication
public class PerfumeEcommercePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfumeEcommercePlatformApplication.class, args);
    }

}
