package com.tailtales.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TailTales 관리자 API 명세서")
                        .description("TailTales 프로젝트의 관리자 관련 API를 정리한 문서입니다.")
                        .version("v1.0.0"));
    }
}
