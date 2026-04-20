package com.express.pickup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.express.pickup.mapper")
public class ExpressPickupApplication {
    public static void main(String[] args) {

        SpringApplication.run(ExpressPickupApplication.class, args);
    }
}
