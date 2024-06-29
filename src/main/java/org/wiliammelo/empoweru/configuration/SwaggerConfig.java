package org.wiliammelo.empoweru.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EmpowerU API")
                        .version("0.1")
                        .description("API documentation for EmpowerU application")
                        .contact(new Contact().email("wiliammelo.mota@gmail.com")
                                                .name("Wiliamm Melo")
                                                .url("https://github.com/wiliammelo01")
                        )
                ).servers(List.of(new Server().url("http://localhost:8080").description("Local server for testing.")));
    }

}
