package com.team4.ttukttak_parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TtukttakParkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtukttakParkingApplication.class, args);
    }

}
