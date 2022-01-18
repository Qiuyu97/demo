package com.qiuyu.demo.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * Swagger 配置
 */
@Configuration
@Profile({"local", "dev","test"})
@EnableOpenApi
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket createRestApi() {
//        List<Response> responseList = new ArrayList<>();
        return new Docket(DocumentationType.OAS_30)
           //     .globalRequestParameters(requestParameterBuilderList())
//                .apiInfo(apiInfo())
                .select()
                // 扫描所有有注解的api
                .apis(RequestHandlerSelectors.basePackage("com.qiuyu"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("系统接口文档")
                .description("系统接口文档")
                .version("1.0.0")
                .build();
    }


}