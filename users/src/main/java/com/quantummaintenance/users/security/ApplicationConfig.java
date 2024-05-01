package com.quantummaintenance.users.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.quantummaintenance.users.controller.UsersAPI;
import com.quantummaintenance.users.entity.*;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;



import lombok.Data;

@Configuration
@Data
public class ApplicationConfig {

	@Autowired
	private UsersAPI userAPI;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				ResponseEntity<Customer> customer=userAPI.getCustomerDetails(username, "");
				return customer.getBody();
			}
			
		};
	}
	
	 @Bean
	  public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService());
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	  }
	 
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }
	  
//	  @Bean
//	    public FirebaseApp initializeFirebase() throws IOException {
//	        FileInputStream serviceAccount =
//	                new FileInputStream("C:\\Users\\Admin\\Desktop\\My Project\\backend\\users\\src\\main\\resources\\upkeep.json");
//
//	        FirebaseOptions options = new FirebaseOptions.Builder()
//	                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//	                .build();
//	        if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
//                FirebaseApp.initializeApp(options);
//            }
//	        return FirebaseApp.initializeApp(options);
//	    }
}
