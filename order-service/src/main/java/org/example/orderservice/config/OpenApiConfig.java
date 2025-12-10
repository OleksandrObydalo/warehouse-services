package org.example.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("API для управління замовленнями на оренду місць складу. " +
                                "Забезпечує створення, затвердження, скасування та управління " +
                                "життєвим циклом замовлень. Інтегрується з Place Service та Payment Service.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Warehouse Management System")
                                .email("support@warehouse.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

