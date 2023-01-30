package com.atguigu.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableTransactionManagement
@EnableDiscoveryClient
@MapperScan("com.atguigu.gulimall.ware.dao")
@SpringBootApplication
public class GilmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GilmallWareApplication.class, args);
    }

}
