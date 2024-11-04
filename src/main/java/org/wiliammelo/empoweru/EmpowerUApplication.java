package org.wiliammelo.empoweru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
public class EmpowerUApplication {

    // TODO: USE PROJECTIONS TO SIMPLFY THE MAPPPING OF ENTITIES TO DTOs
    public static void main(String[] args) {
        SpringApplication.run(EmpowerUApplication.class, args);
    }

}
