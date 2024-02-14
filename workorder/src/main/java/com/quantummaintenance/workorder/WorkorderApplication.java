package com.quantummaintenance.workorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WorkorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkorderApplication.class, args);
	}
	@Bean
    WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	            	registry.addMapping("/**")
	                .allowedOrigins("**")
//	                .allowedOrigins("**")
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*", "Authorization","Content-Type", "Date", "Total-Count", "loginInfo","jwt_token")
                    .exposedHeaders("Content-Type", "Date", "Total-Count", "loginInfo", "jwt_token","Authorization")
	                .allowCredentials(true)
	                .maxAge(3600);
	            }
	        };
	    }

}
