package com.classes.Backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myClassesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Classes API")
                        .description("REST API documentation for My Classes - Educational Institute Discovery and Management Platform")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("My Classes Team")
                                .email("support@myclasses.co.in")
                                .url("https://myclasses.co.in"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.myclasses.co.in").description("Production Server")));
    }
}
