package com.iremayvaz.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("STOK TAKİP SİSTEMİ")
                .description("Kullanıcı girişi gerektiren ürün stok takip sistemi.")
                        .contact(new Contact()
                                .name("İrem AYVAZ")
                                .email("iremayvazz28@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth", "scope"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("JWT Token değeri girilmeli!")));
    }
}

