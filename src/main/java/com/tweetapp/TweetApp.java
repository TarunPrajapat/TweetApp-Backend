package com.tweetapp;

import java.util.Collections;

import javax.management.loading.PrivateClassLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class TweetApp {

	public static void main(String[] args) {
		SpringApplication.run(TweetApp.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.any())
				.apis(RequestHandlerSelectors
				.basePackage("com.tweetapp"))
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Tweet APP Api",
				"An application where user can post, like and comment a tweet, at the same time search users and their tweets.",
				"1.0", "Free to use",
				new springfox.documentation.service.Contact("Parichay Gupta", "https://github.com/parichaygupta",
						"parichaygupta93@gmail.com"),
				"PG license", "https://github.com/parichaygupta", Collections.emptyList());
	}
	


}
