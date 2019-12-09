package it.unical.demacs.asde.signme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unical.demacs.asde.signme.services.FaceRecognitionService;

@SpringBootApplication
public class SignMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignMeApplication.class, args);
		FaceRecognitionService faceRecognitionService = new FaceRecognitionService();
		// faceRecognitionService.run();
	}

}