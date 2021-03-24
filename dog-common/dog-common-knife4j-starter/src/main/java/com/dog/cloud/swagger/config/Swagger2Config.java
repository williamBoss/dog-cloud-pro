package com.dog.cloud.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * SwaggerConfig
 *
 * @author KING
 */
@Configuration
@EnableOpenApi
@EnableKnife4j
@Profile("!prod")
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Config {

    @Bean(value = "userApi")
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            // 将api的元信息设置为包含在json ResourceListing响应中。
            .apiInfo(apiInfo())
            // 选择哪些接口作为swagger的doc发布
            .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any())
            .build()
            // 授权信息设置，必要的header token等认证信息
            .securitySchemes(securitySchemes())
            // 授权信息全局应用
            .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("cdms-pro 接口文档").description("用于接口查看及调试")
            .contact(new Contact("cdms-pro", "", "")).version("2021.0.0").build();
    }

    /**
     * 设置授权信息
     */
    private List<SecurityScheme> securitySchemes() {
        ApiKey apiKey = new ApiKey("Bearer TOKEN", "Authorization", "header");
        return Collections.singletonList(apiKey);
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder().securityReferences(Collections.singletonList(
            new SecurityReference("Bearer TOKEN",
                new AuthorizationScope[] {new AuthorizationScope("global", "accessEverything")}))).build());
    }

}