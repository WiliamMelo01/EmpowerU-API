package org.wiliammelo.empoweru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmpowerUApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmpowerUApplication.class, args);
    }

}
