package com.quantumai.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableWebMvc // Ensure Web MVC is enabled for global CORS configuration
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:4200") // Allow requests from this origin
//                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
//                        .allowedHeaders("*") // Allow all headers
//                        .exposedHeaders("Authorization") // Expose additional headers if needed
//                        .allowCredentials(true); // Allow sending credentials (e.g., cookies)
//            }
//        };
//    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new
//                UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
    @Bean
    WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	            	registry.addMapping("/**")
	                .allowedOrigins("http://localhost:4200")
//	                .allowedOrigins("**")
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*", "Authorization")
	                .exposedHeaders("Authorization")
	                .allowCredentials(true)
	                .maxAge(3600);
	            }
	        };
	    }
}
