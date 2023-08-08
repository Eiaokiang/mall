package com.eiaokiang.way;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eiaokiang.way.mapper")
public class WayApplication {

    public static void main(String[] args) {
        SpringApplication.run(WayApplication.class, args);
    }

}
