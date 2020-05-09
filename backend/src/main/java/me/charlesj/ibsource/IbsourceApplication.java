package com.linkage.ibsource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.linkage.ibsource.dao")
@SpringBootApplication
public class IbsourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IbsourceApplication.class, args);
    }
}
