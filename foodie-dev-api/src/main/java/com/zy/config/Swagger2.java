package com.zy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    //http://localhost:8088/swagger-ui.html 原路径
    //http://localhost:8088/doc.html 非官方路径
    //配置swagger2 核心配置
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定api类型为swagger2
        .apiInfo(apiInfo())//用于指定api文档汇总信息
        .select().apis(RequestHandlerSelectors.basePackage("com.zy.controller"))//指定controller包
        .paths(PathSelectors.any())//所有controller
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("天天吃货 单上平台api")//文档页面标题
                .contact(new Contact("zy", "www.zy.com", "181332969@qq.com"))//联系人
                .description("专为天天吃货提供的api文档")//详细信息
                .version("1.0.1")//文档版本号
                .termsOfServiceUrl("www.zy.com")//网站地址
                .build();

    }
}
