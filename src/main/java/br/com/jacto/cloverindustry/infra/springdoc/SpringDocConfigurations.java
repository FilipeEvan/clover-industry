package br.com.jacto.cloverindustry.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpeanAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Indústria Trevo S.A.")
                        .description("API da Indústria Trevo S.A., contendo a funcionalidade de cadastro e " +
                                "consultas dos produtos oferecidos por ela, além de oferecer recurso que possibilite " +
                                "a solicitação de proposta comercial e colete dados de possíveis clientes.")
                        .contact(new Contact()
                                .name("Filipe Evangelista Avila")
                                .email("filipeevan@outlook.com"))
                        .license(new License()
                                .name("Apache 2.0")));
    }

}
