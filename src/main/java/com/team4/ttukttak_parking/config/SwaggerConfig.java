package com.team4.ttukttak_parking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ttukttakParkingAPI() {

        return new OpenAPI()
            .info(getinfo())
            .components(new Components()
            );
    }

    private Info getinfo() {
        return new Info()
            .title("뚝딱파킹 API")
            .description("NBE2_3_2_TEAM4_뚝딱파킹 API 명세")
            .version("0.0.1");
    }

}
