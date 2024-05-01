package com.quantummaintenance.users;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class UsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
		 try {
	            FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("upkeep.json").getInputStream()))
	                    .setDatabaseUrl("https://upkeep-22aee.firebaseio.com")
	                    .build();
	            if(FirebaseApp.getApps().isEmpty()) { //<--- check with  this line
	                FirebaseApp.initializeApp(options);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
//	@PostConstruct
//    public void initializeFirebase() throws IOException {
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.getApplicationDefault())
//                .setDatabaseUrl("https://upkeep-22aee.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options, "upkeep");
//    }

}
