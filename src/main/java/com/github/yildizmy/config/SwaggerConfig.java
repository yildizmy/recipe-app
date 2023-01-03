package com.github.yildizmy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration file used to configure Swagger
 */
@Configuration
public class SwaggerConfig {

    private static final String TITLE = "Recipe API";
    private static final String DESCRIPTION = "Application used for managing Recipe data";
    private static final String VERSION = "1.0";
    private static final String TERMS_OF_SERVICE_URL = null;
    private static final String LICENSE = null;
    private static final String LICENSE_URL = null;
    private static final List<VendorExtension> VENDOR_EXTENSIONS = new ArrayList<>();
    private static final Contact CONTACT = new Contact(null, null, null);

    private ApiInfo apiInfo() {
        return new ApiInfo(TITLE, DESCRIPTION, VERSION, TERMS_OF_SERVICE_URL,
                CONTACT, LICENSE, LICENSE_URL, VENDOR_EXTENSIONS);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
