package com.djukim.thisnthat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.djukim"})
@ConfigurationPropertiesScan(basePackages = {"com.djukim"})
public class ThisnthatApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThisnthatApiApplication.class, args);
    }
}
