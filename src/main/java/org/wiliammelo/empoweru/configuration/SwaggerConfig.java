package org.wiliammelo.empoweru.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * This class sets up the OpenAPI documentation for the EmpowerU API, including API information and server details.
 */
@Configuration
public class SwaggerConfig {

    @Value("${local.server.host}")
    private String localServerHost;

    /**
     * Creates and configures the OpenAPI bean with API metadata and server information.
     * The API information includes title, version, description, and contact details.
     * Server information specifies the URL and description of the server where the API is hosted.
     *
     * @return The configured OpenAPI instance.
     */
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
                ).servers(List.of(
                        new Server().url(localServerHost).description("Production server for EmpowerU application."),
                        new Server().url("http://localhost:8080").description("Local server for testing."))
                );
    }

}
