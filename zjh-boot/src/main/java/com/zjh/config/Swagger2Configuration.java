package com.zjh.config;

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
public class Swagger2Configuration {
	
	@Bean
	public Docket accessToken() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("api")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.zjh.controller"))
				.paths(PathSelectors.regex("/*/.*"))
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Spring Boot 之 Web 篇")
				.description("spring boot Web 相关内容")
				.termsOfServiceUrl("http://www.extlight.com")
				.contact(new Contact("moonlightL", "http://www.extlight.com", "445847261@qq.com"))
				.version("1.0")
				.build();
	}

}
