package com.diplomathesis.mMusic_Backend;

import java.io.FileInputStream;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MMusicBackendApplication {

	public static void main(String[] args) {

		// Initialize connection to Cloud Firestore Database from Firebase
		try {
			FileInputStream serviceAccount = new FileInputStream(
					"C:/IG_3_UNI/LICENTA/APLICATIE/mMusic_Backend/src/main/resources/mmusic-web-app-firebase-adminsdk-u8gb0-b9debc5bee.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://mmusic-web-app.firebaseio.com")
					.build();

			FirebaseApp.initializeApp(options);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// run Spring Boot Application
		SpringApplication.run(MMusicBackendApplication.class, args);
	}
}
