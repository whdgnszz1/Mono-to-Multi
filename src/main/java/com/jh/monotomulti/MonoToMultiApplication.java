package com.jh.monotomulti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jh.monotomulti", "com.jh.common"})
public class MonoToMultiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonoToMultiApplication.class, args);
    }

}
